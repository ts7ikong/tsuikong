package cc.mrbird.febs.server.tjdkxm.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/13 17:01
 */
public class CacheableServiceUtils {
    
    /**
     * 对项目结果集封装
     *
     * @param projectModels
     * @return {@link Map< String, Object>}
     */
    public Map<String, Object> projectChoose(List<Map<String, Object>> projectModels) {
        Map<String, Object> projectMap = new HashMap<>(2);
        List<HashMap<String, Object>> unitenginesMap = new ArrayList<>(2);
        if (projectModels.size() == 0) {
            return Collections.emptyMap();
        }
        projectMap.put("projectId", projectModels.get(0).get("PROJECT_ID"));
        projectMap.put("projectName", projectModels.get(0).get("PROJECT_NAME"));
        Set<Object> unitengineIds =
                projectModels.stream().map(model -> model.get("unitengineId")).collect(Collectors.toSet());
        unitengineIds.forEach(unitengineId -> {
            if (unitengineId != null) {
                HashMap<String, Object> unitengineMap = new HashMap<>(2);
                List<HashMap<String, Object>> parcelsMap = new ArrayList<>(2);
                Set<Long> parcelIds =
                        projectModels.stream().map(model -> {
                            Long unitengineId2 = (Long) model.get("unitengineId");
                            if (unitengineId.equals(unitengineId2)) {
                                return (Long) model.get("PARCEL_ID");
                            }
                            return -1L;
                        }).collect(Collectors.toSet());
                projectModels.forEach(model2 -> {
                    Long unitengineId1 = (Long) model2.get("unitengineId");
                    if (unitengineId.equals(unitengineId1)) {
                        unitengineMap.put("unitengineId", model2.get("unitengineId"));
                        unitengineMap.put("unitengineName", model2.get("unitengineName"));
                        Long parcelId2 = (Long) model2.get("PARCEL_ID");
                        if (parcelId2 != null) {
                            if (parcelIds.size() > 0 && parcelIds.contains(parcelId2)) {
                                parcelIds.remove(parcelId2);
                                HashMap<String, Object> parcelMap = new HashMap<>(2);
                                parcelMap.put("parcelId", parcelId2);
                                parcelMap.put("parcelName", model2.get("PARCEL_NAME"));
                                List<HashMap<String, Object>> subitemsMap = new ArrayList<>(2);
                                projectModels.forEach(model3 -> {
                                    Long parcelId3 = (Long) model3.get("PARCEL_ID");
                                    if (parcelId2.equals(parcelId3)) {
                                        HashMap<String, Object> subitemMap = new HashMap<>(2);
                                        Object subitemId = model3.get("SUBITEM_ID");
                                        if (subitemId != null) {
                                            subitemMap.put("subitemName", model3.get("SUBITEM_NAME"));
                                            subitemMap.put("subitemId", subitemId);
                                            subitemsMap.add(subitemMap);
                                        }
                                    }
                                });
                                if (subitemsMap.size() > 0) {
                                    parcelMap.put("subitems", subitemsMap);
                                }
                                if (parcelMap.size() > 0) {
                                    parcelsMap.add(parcelMap);
                                }
                            }
                        }
                    }
                });
                if (parcelsMap.size() > 0) {
                    unitengineMap.put("parcels", parcelsMap);
                }
                if (unitengineMap.size() > 0) {
                    unitenginesMap.add(unitengineMap);
                }
            }
        });
        if (unitenginesMap.size() > 0) {
            projectMap.put("unitengines", unitenginesMap);
        }
        return projectMap;
    }
}
