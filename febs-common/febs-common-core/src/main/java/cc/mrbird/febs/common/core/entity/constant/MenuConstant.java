package cc.mrbird.febs.common.core.entity.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/23 16:29
 */
public class MenuConstant {
    public static final Map<String, Integer[]> FRONT_ENCRY_MENU = new HashMap<>(8);
    static {
        FRONT_ENCRY_MENU.put("456DE2002611509CF1BE4FD8726D6B6A", new Integer[] {50, 156});
        FRONT_ENCRY_MENU.put("0C5248BAE0161601099DB01DDE1708E2", new Integer[] {52, 164});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCA", new Integer[] {56, 173});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCB", new Integer[] {58, 181});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCD", new Integer[] {60, 190});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCE", new Integer[] {35, 291});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCF", new Integer[] {36, 108});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCR", new Integer[] {37, 112});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCS", new Integer[] {38, 116});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCG", new Integer[] {39, 120});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCH", new Integer[] {40, 124});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCI", new Integer[] {41, 128});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCJ", new Integer[] {42, 132});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCK", new Integer[] {43, 136});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCL", new Integer[] {44, 140});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCM", new Integer[] {45, 144});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCN", new Integer[] {46, 148});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCO", new Integer[] {62, 193});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCP", new Integer[] {63, 196});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCQ", new Integer[] {64, 199});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCT", new Integer[] {75, 226});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FCV", new Integer[] {79, 299});
        FRONT_ENCRY_MENU.put("7923F440AC687D7125EDBF1728687FC5", new Integer[] {48, 152});
    }
    /**
     * 公共
     */
    public static final String PUBLIC_MENU = "1";
    /**
     * 项目
     */
    public static final String PROJECT_MENU = "2";
    /**
     * 后台管理
     */
    public static final String SYSTEM = "system";
    public static final int SYSTEM_ID = 1;
    /**
     * 用户管理
     */
    public static final String USER = "user";
    public static final int USER_ID = 2;
    /**
     * 角色管理
     */
    public static final String ROLE = "role";
    public static final int ROLE_ID = 3;
    /**
     * 部门管理
     */
    public static final String DEPARTMENT = "department";
    public static final int DEPARTMENT_ID = 4;
    /**
     * 临聘人员
     */
    public static final String TEMPORARY_ACCOUNTANT = "temporaryAccountant";
    public static final int TEMPORARY_ACCOUNTANT_ID = 5;
    /**
     * 系统通知
     */
    public static final String SYSTEM_NOTIFICATION = "system_notification";
    public static final int SYSTEM_NOTIFICATION_ID = 6;
    /**
     * 反馈信息
     */
    public static final String FEEDBACK_INFO = "feedbackInfo";
    public static final int FEEDBACK_INFO_ID = 7;
    /**
     * 图片管理
     */
    public static final String BANNER = "banner";
    public static final int BANNER_ID = 8;
    /**
     * 操作日志
     */
    public static final String OPERATION_LOG = "operationLog";
    public static final int OPERATION_LOG_ID = 9;
    /**
     * 档案模板
     */
    public static final String FILETEMPLATE = "filetemplate";
    public static final int FILETEMPLATE_ID = 10;
    /**
     * 合同模板
     */
    public static final String CONTRACTTEMPLATE1 = "contractTemplate1";
    public static final int CONTRACTTEMPLATE1_ID = 11;
    /**
     * 合同档案库
     */
    public static final String CONTRACTARCHIVE1 = "contractArchive1";
    public static final int CONTRACTARCHIVE1_ID = 12;
    /**
     * 施工档案
     */
    public static final String CONSTRUCTIONARCHIVES1 = "constructionArchives1";
    public static final int CONSTRUCTIONARCHIVES1_ID = 13;
    /**
     * 技术台账
     */
    public static final String TECHNICALACCOUNT1 = "technicalAccount1";
    public static final int TECHNICALACCOUNT1_ID = 14;
    /**
     * 技术标准
     */
    public static final String TECHNICALSTANDARD1 = "technicalStandard1";
    public static final int TECHNICALSTANDARD1_ID = 15;
    /**
     * 技术交底
     */
    public static final String TECHNICALDISCLOSURE1 = "technicalDisclosure1";
    public static final int TECHNICALDISCLOSURE1_ID = 16;
    /**
     * 交接资料
     */
    public static final String HANDOVERDATA1 = "handoverData1";
    public static final int HANDOVERDATA1_ID = 17;
    /**
     * 工程确认单
     */
    public static final String CONFIRMATIONSHEET1 = "confirmationSheet1";
    public static final int CONFIRMATIONSHEET1_ID = 18;
    /**
     * 工程联络单
     */
    public static final String LIAISONSHEET1 = "liaisonSheet1";
    public static final int LIAISONSHEET1_ID = 19;
    /**
     * 设计变更
     */
    public static final String DESIGNCHANGE1 = "designChange1";
    public static final int DESIGNCHANGE1_ID = 20;
    /**
     * 测绘资料
     */
    public static final String SURVEYINGMAPPING1 = "surveyingMapping1";
    public static final int SURVEYINGMAPPING1_ID = 21;
    /**
     * 竣工资料
     */
    public static final String COMPLETIONDATA1 = "completionData1";
    public static final int COMPLETIONDATA1_ID = 22;
    /**
     * 数字化中心
     */
    public static final String DIGITAL1 = "digital1";
    public static final int DIGITAL1_ID = 23;
    /**
     * 党建中心
     */
    public static final String PARTY = "party";
    public static final int PARTY_ID = 24;
    /**
     * 党建资料
     */
    public static final String PARTY_MATERIALS = "partyMaterials";
    public static final int PARTY_MATERIALS_ID = 25;
    /**
     * 宣传学习
     */
    public static final String PARTY_LEARNING = "partyLearning";
    public static final int PARTY_LEARNING_ID = 26;
    /**
     * 综合治理
     */
    public static final String PARTYCONTROL = "partycontrol";
    public static final int PARTYCONTROL_ID = 27;
    /**
     * 项目管理
     */
    public static final String PROJECTS = "projects";
    public static final int PROJECTS_ID = 28;
    /**
     * 项目列表
     */
    public static final String PROJECTINFO = "projectinfo";
    public static final int PROJECTINFO_ID = 29;
    /**
     * 单位工程(页面)
     */
    public static final String UNITPROJECT = "unitproject";
    public static final int UNITPROJECT_ID = 30;
    /**
     * 分部工程
     */
    public static final String DECOMPOSITION = "decomposition";
    public static final int DECOMPOSITION_ID = 31;
    /**
     * 分项工程
     */
    public static final String ITEMIZED = "itemized";
    public static final int ITEMIZED_ID = 32;
    /**
     * 供应商单位
     */
    public static final String DATA_MANAGEMENT = "dataManagement";
    public static final int DATA_MANAGEMENT_ID = 33;
    /**
     * 项目资料
     */
    public static final String ARCHIVES = "archives";
    public static final int ARCHIVES_ID = 34;
    /**
     * 合同档案库
     */
    public static final String CONTRACT_ARCHIVE = "contractArchive";
    public static final int CONTRACT_ARCHIVE_ID = 35;
    /**
     * 施工档案
     */
    public static final String CONSTRUCTION_ARCHIVES = "constructionArchives";
    public static final int CONSTRUCTION_ARCHIVES_ID = 36;
    /**
     * 技术台账
     */
    public static final String TECHNICAL_ACCOUNT = "technicalAccount";
    public static final int TECHNICAL_ACCOUNT_ID = 37;
    /**
     * 技术标准
     */
    public static final String TECHNICAL_STANDARD = "technicalStandard";
    public static final int TECHNICAL_STANDARD_ID = 38;
    /**
     * 技术交底
     */
    public static final String TECHNICAL_DISCLOSURE = "technicalDisclosure";
    public static final int TECHNICAL_DISCLOSURE_ID = 39;
    /**
     * 交接资料
     */
    public static final String HANDOVER_DATA = "handoverData";
    public static final int HANDOVER_DATA_ID = 40;
    /**
     * 工程确认单
     */
    public static final String CONFIRMATION_SHEET = "confirmationSheet";
    public static final int CONFIRMATION_SHEET_ID = 41;
    /**
     * 工程联络单
     */
    public static final String LIAISON_SHEET = "liaisonSheet";
    public static final int LIAISON_SHEET_ID = 42;
    /**
     * 设计变更
     */
    public static final String DESIGN_CHANGE = "designChange";
    public static final int DESIGN_CHANGE_ID = 43;
    /**
     * 测绘资料
     */
    public static final String SURVEYING_MAPPING = "surveyingMapping";
    public static final int SURVEYING_MAPPING_ID = 44;
    /**
     * 竣工资料
     */
    public static final String COMPLETION_DATA = "completionData";
    public static final int COMPLETION_DATA_ID = 45;
    /**
     * 照片管理
     */
    public static final String PHOTO_MANAGEMENT = "photoManagement";
    public static final int PHOTO_MANAGEMENT_ID = 46;
    /**
     * 质量管理
     */
    public static final String QUALITY = "quality";
    public static final int QUALITY_ID = 47;
    /**
     * 质量规范标准资料
     */
    public static final String STANDARD_DATA = "standardData";
    public static final int STANDARD_DATA_ID = 48;
    /**
     * 质量问题图表
     */
    public static final String PROBLEM_CHART = "problemChart";
    public static final int PROBLEM_CHART_ID = 49;
    /**
     * 质量检查计划
     */
    public static final String INSPECTION_PLAN = "inspectionPlan";
    public static final int INSPECTION_PLAN_ID = 50;
    /**
     * 质量检查记录
     */
    public static final String INSPECTION_RECORD = "inspectionRecord";
    public static final int INSPECTION_RECORD_ID = 51;
    /**
     * 质量问题清单
     */
    public static final String PROBLEM_LIST = "problemList";
    public static final int PROBLEM_LIST_ID = 52;
    /**
     * 质量整改记录
     */
    public static final String RECTIFICATION_RECORD = "rectificationRecord";
    public static final int RECTIFICATION_RECORD_ID = 53;
    /**
     * 安全管理
     */
    public static final String SECURITY = "security";
    public static final int SECURITY_ID = 54;
    /**
     * 安全隐患图表
     */
    public static final String DANGER_CHART = "dangerChart";
    public static final int DANGER_CHART_ID = 55;
    /**
     * 安全检查计划
     */
    public static final String SINSPECTION_PLAN = "sinspectionPlan";
    public static final int SINSPECTION_PLAN_ID = 56;
    /**
     * 安全检查记录
     */
    public static final String INSPECTION_RECORDQ = "inspectionRecordq";
    public static final int INSPECTION_RECORDQ_ID = 57;
    /**
     * 安全隐患清单
     */
    public static final String DANGERS_LIST = "dangersList";
    public static final int DANGERS_LIST_ID = 58;
    /**
     * 安全整改记录
     */
    public static final String SRECTIFICATION_RECORD = "srectificationRecord";
    public static final int SRECTIFICATION_RECORD_ID = 59;
    /**
     * 重大危险源记录
     */
    public static final String INSTALLATIONS_MAJOR = "installationsMajor";
    public static final int INSTALLATIONS_MAJOR_ID = 60;
    /**
     * 项目日志
     */
    public static final String LOG = "log";
    public static final int LOG_ID = 61;
    /**
     * 施工日志
     */
    public static final String CONSTRUCTION = "construction";
    public static final int CONSTRUCTION_ID = 62;
    /**
     * 重大事项日志
     */
    public static final String majorProjectLog = "majorProjectLog";
    public static final int MAJOR_PROJECT_LOG_ID = 63;
    /**
     * 专职安全管理人员日志
     */
    public static final String PERSONNEL = "personnel";
    public static final int PERSONNEL_ID = 64;
    /**
     * 办公管理
     */
    public static final String OFFICE = "office";
    public static final int OFFICE_ID = 65;
    /**
     * 工作汇报
     */
    public static final String WORK_REPORT = "workReport";
    public static final int WORK_REPORT_ID = 66;
    /**
     * 工作审批
     */
    public static final String WORK_APPROVAL = "workApproval";
    public static final int WORK_APPROVAL_ID = 67;
    /**
     * 请假申请
     */
    public static final String LEAVE_APPLICATION = "leaveApplication";
    public static final int LEAVE_APPLICATION_ID = 68;
    /**
     * 请假审批
     */
    public static final String LEAVE_APPROVAL = "leaveApproval";
    public static final int LEAVE_APPROVAL_ID = 69;
    /**
     * 会议管理
     */
    public static final String MANAGEMENT_MEETINGS = "managementMeetings";
    public static final int MANAGEMENT_MEETINGS_ID = 70;
    /**
     * 日常管理
     */
    public static final String DAILYMANAGEMENT = "dailymanagement";
    public static final int DAILYMANAGEMENT_ID = 71;
    /**
     * 考勤管理
     */
    public static final String USERPUNCH = "userpunch";
    public static final int USERPUNCH_ID = 72;
    /**
     * 招投标文件
     */
    public static final String BIDDINGDOCUMENTS = "biddingdocuments";
    public static final int BIDDINGDOCUMENTS_ID = 73;
    /**
     * 业绩统计
     */
    public static final String PERFORMANCESTATISTICS = "performancestatistics";
    public static final int PERFORMANCESTATISTICS_ID = 74;
    /**
     * 院务文件
     */
    public static final String CONFERENCE = "conference";
    public static final int CONFERENCE_ID = 75;
    /**
     * 三重一大会议
     */
    public static final String THREECONFERENCE = "conference";
    public static final int THREE_CONFERENCE_ID = 80;
    /**
     * 项目成员(页面)
     */
    public static final String ADDEMPLOYEE = "addemployee";
    public static final int ADDEMPLOYEE_ID = 78;
    public static final String OVERTIME_APPROVAL = "overtimeApproval";
    public static final int OVERTIME_APPROVAL_ID = 0;
    /**
     * 上级下达文件
     */
    public static final String SUPERIOR = "addemployee";
    public static final int SUPERIOR_ID = 79;

}
