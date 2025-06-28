package er.rennala.advice.domain.ref;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Aspect
public class RefProcessor {

    @Around("@annotation(er.rennala.advice.domain.ref.RefAnalyst) || @within(er.rennala.advice.domain.ref.RefAnalyst)")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returned = joinPoint.proceed();
        handle(returned);
        return returned;
    }

    private void handle(Object obj) {
        if (Objects.isNull(obj)) {
            return;
        }
        if (obj instanceof Collection<?>) {
            Collection<Object> c = (Collection<Object>) obj;
            if (!c.isEmpty()) {
                Class<?> objectClass = c.iterator().next().getClass();
                FieldsGroupingByAnnotation groupingFields = groupingByAnnotations(objectClass.getDeclaredFields());
                for (Field field : groupingFields.withRefAnnotation) {
                    handleCollectionReturn(field, c);
                }
                for (Field field : groupingFields.withDeepRefAnnotation) {
                    field.setAccessible(true);
                    List<Object> deepObjects = new ArrayList<>();
                    c.forEach(o -> deepObjects.addAll(getFieldValueAsList(field, o)));
                    handle(deepObjects);
                }
            }
        } else {
            Class<?> objectClass = obj.getClass();
            FieldsGroupingByAnnotation groupingFields = groupingByAnnotations(objectClass.getDeclaredFields());
            for (Field field : groupingFields.withRefAnnotation) {
                handleSingleReturn(field, obj);
            }
            for (Field field : groupingFields.withDeepRefAnnotation) {
                field.setAccessible(true);
                List<Object> deepObjects = getFieldValueAsList(field, obj);
                handle(deepObjects);
            }
        }
    }

    private FieldsGroupingByAnnotation groupingByAnnotations(Field[] fields) {
        FieldsGroupingByAnnotation v = new FieldsGroupingByAnnotation();
        if (Objects.isNull(fields)) {
            return v;
        }
        for (Field field : fields) {
            if (field.isAnnotationPresent(Ref.class)) {
                v.withRefAnnotation.add(field);
            }
            if (field.isAnnotationPresent(DeepRef.class)) {
                v.withDeepRefAnnotation.add(field);
            }
        }
        return v;
    }

    private void handleSingleReturn(Field fieldWithRefAnnotation, Object singleReturn) {
        fieldWithRefAnnotation.setAccessible(true);
        Ref ref = fieldWithRefAnnotation.getAnnotation(Ref.class);
        RefAnalyzer refAnalyzer = SpringUtil.getBean(ref.analyzer());

        // Get the referencedFieldValue.
        List<Object> referencedFieldValues = getFieldValueAsList(fieldWithRefAnnotation, singleReturn);
        // Find referenced objects by referencedFieldValues.
        List<Object> referencedObjects;
        if (referencedFieldValues.size() == 1) {
            referencedObjects = new ArrayList<>();
            Object referencedObject = refAnalyzer.findOne(referencedFieldValues.get(0));
            if (Objects.nonNull(referencedObject)) {
                referencedObjects.add(referencedObject);
            }
        } else {
            referencedObjects = refAnalyzer.findMany(referencedFieldValues);
        }

        // Mapping.
        Map<Object, Object> referencedField2Object = mappingReferencedObjects(ref, referencedObjects);

        // Get the referenced object by referenced field and then set into the single record.
        setReferencedObjectToSingleReturn(fieldWithRefAnnotation, singleReturn, referencedField2Object, ref, refAnalyzer);
    }

    private void handleCollectionReturn(Field fieldWithRefAnnotation, Collection<Object> collectionReturn) {
        fieldWithRefAnnotation.setAccessible(true);
        Ref ref = fieldWithRefAnnotation.getAnnotation(Ref.class);
        RefAnalyzer refAnalyzer = SpringUtil.getBean(ref.analyzer());

        // Aggregate the referencedFieldValue of each record together.
        List<Object> referencedFieldValues = new ArrayList<>();
        for (Object singleRecord : collectionReturn) {
            referencedFieldValues.addAll(getFieldValueAsList(fieldWithRefAnnotation, singleRecord));
        }
        // Find referenced objects by referencedFieldValues.
        List<Object> referencedObjects;
        if (referencedFieldValues.size() == 1) {
            referencedObjects = new ArrayList<>();
            Object referencedObject = refAnalyzer.findOne(referencedFieldValues.get(0));
            if (Objects.nonNull(referencedObject)) {
                referencedObjects.add(referencedObject);
            }
        } else {
            referencedObjects = refAnalyzer.findMany(referencedFieldValues);
        }

        // Mapping.
        Map<Object, Object> referencedField2Object = mappingReferencedObjects(ref, referencedObjects);

        // Get the referenced object by referenced field and then set into each record.
        collectionReturn.forEach(r -> setReferencedObjectToSingleReturn(fieldWithRefAnnotation, r, referencedField2Object, ref, refAnalyzer));
    }

    /**
     * Get the value of spec field. e.g.
     */
    private List<Object> getFieldValueAsList(Field field, Object obj) {
        List<Object> values = new ArrayList<>();
        try {
            Object filedValue = field.get(obj);
            // The field type maybe collection or not.
            if (filedValue instanceof Collection) {
                values.addAll((Collection<Object>) filedValue);
            } else {
                values.add(filedValue);
            }
        } catch (IllegalAccessException e) {
            log.error("[] Get field value failed. fieldName: {}", field.getName());
            log.error("[] ", e);
            throw new RuntimeException(e);
        }
        return values;
    }

    /**
     * Mapping the referenced objects by ref. e.g.
     * ref: @Ref(analyzer = UserRepositoryImpl::class, refField = "id", analysisTo = "players")
     * referencedObjects: [User(id=1, name=...), User(id=2, name=...), User(id=3, name=...)]
     * return: {1=User(id=1, name=...), 2=User(id=2, name=...), 3=User(id=3, name=...)}
     *
     * @param ref               the @Ref annotation.
     * @param referencedObjects the referenced objects.
     * @return map.
     */
    private Map<Object, Object> mappingReferencedObjects(Ref ref, List<Object> referencedObjects) {
        Map<Object, Object> referencedField2Object = new HashMap<>();
        for (Object o : referencedObjects) {
            try {
                Field f = o.getClass().getDeclaredField(ref.refField());
                f.setAccessible(true);
                referencedField2Object.put(f.get(o), o);
            } catch (NoSuchFieldException e) {
                log.error("[] Get field failed. fieldName: {}", ref.refField());
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return referencedField2Object;
    }

    /**
     * Set the referenced object to the single return. e.g.
     * fieldWithRefAnnotation: Field(name=userIds)
     * singleReturn: {Post(userIds=[1, 2, 3]), content=...}
     * referencedField2Object: {1=User(id=1, name=...), 2=User(id=2, name=...), 3=User(id=3, name=...)}
     * ref: @Ref(analyzer = UserRepositoryImpl::class, refField = "id", analysisTo = "players")
     * refAnalyzer: instance of UserRepositoryImpl
     *
     * @param fieldWithRefAnnotation the field with @Ref annotation.
     * @param singleReturn           the original record.
     * @param referencedField2Object the mapping.
     * @param ref                    the @Ref annotation.
     * @param refAnalyzer            the refAnalyzer
     */
    private void setReferencedObjectToSingleReturn(Field fieldWithRefAnnotation, Object singleReturn, Map<Object, Object> referencedField2Object, Ref ref, RefAnalyzer refAnalyzer) {
        try {
            Object filedValue = fieldWithRefAnnotation.get(singleReturn);
            Object referencedObject;
            // The field(playerIds) maybe collection or not.
            if (Collection.class.isAssignableFrom(fieldWithRefAnnotation.getType())) {
                Collection<Object> collectionFieldValue = (Collection<Object>) filedValue;
                referencedObject = collectionFieldValue.stream().map(key -> {
                    Object o = referencedField2Object.get(key);
                    if (Objects.isNull(o)) {
                        o = refAnalyzer.fallback(key);
                    }
                    return refAnalyzer.convert(o);
                }).filter(Objects::nonNull).toList();
            } else {
                Object o = referencedField2Object.get(filedValue);
                if (Objects.isNull(o)) {
                    o = refAnalyzer.fallback(filedValue);
                }
                referencedObject = refAnalyzer.convert(o);
            }
            Field targetField = singleReturn.getClass().getDeclaredField(ref.analysisTo());
            targetField.setAccessible(true);
            targetField.set(singleReturn, referencedObject);
        } catch (IllegalAccessException e) {
            log.error("[] Get field value failed. fieldName: {}", fieldWithRefAnnotation.getName());
            log.error("[] ", e);
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            log.error("[] Get field failed. fieldName: {}", ref.analysisTo());
            throw new RuntimeException(e);
        }
    }

    private static class FieldsGroupingByAnnotation {

        private final List<Field> withRefAnnotation = new ArrayList<>();

        private final List<Field> withDeepRefAnnotation = new ArrayList<>();

    }

}