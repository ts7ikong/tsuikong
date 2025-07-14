package cc.mrbird.febs.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

/**
 * 导入导出测试，Eximport = export + import
 *
 * @author MrBird
 */
@Data
@TableName("p_correct")
@Excel("导入矫正对象")
public class EximportCorrect {

    /**
     * 姓名
     */
    @ExcelField(value = "姓名", required = true, maxLength = 20,
            comment = "提示：必填，长度不能超过20个字符")
    private String realname;

    /**
     * 曾用名
     */
    @ExcelField(value = "曾用名", required = true, maxLength = 20,
            comment = "提示：必填，长度不能超过20个字符")
    private String onceName;

    /**
     * 个人联系电话
     */
    @ExcelField(value = "个人联系电话", required = true, regularExp = "^([1]\\d{10}|([\\(（]?0[0-9]{2,3}[）\\)]?[-]?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)$",
            comment = "提示：电话号码格式错误" )
    private String linkPhone;

    /**
     * 用户名
     */
    @ExcelField(value = "用户名", required = true, maxLength = 20,
            comment = "提示：必填，长度不能超过20个字符" )
    private String userName;

    /**
     * 身份证号
     */
    @ExcelField(value = "身份证号", required = true, regularExp = "^[1-9]\\d{5}(18|19|20|(3\\d))\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",
            comment = "提示：身份证号只能是数字" )
    private String idNumber;

    /**
     * 性别 0男 1女 2保密
     */
    @ExcelField(value = "性别", required = true, maxLength = 2, writeConverterExp = "男=0,女=1,保密=2",
            comment = "提示：必填，只能是男、女、保密" )
    private Integer ssex;

    /**
     * 民族
     */
    @ExcelField(value = "民族", required = true,
            comment = "提示：必填" )
    private String ethnic;

    /**
     * 出生年月日
     */
    @ExcelField(value = "出生年月日", required = true, regularExp = "\\d{4}-\\d{2}-\\d{2}",
            comment = "提示：必填，出生年月日格式为：2020-01-01" )
    private String birthday;

    /**
     * 文化程度
     */
    @ExcelField(value = "文化程度", required = true,
            comment = "提示：文化程度必填" )
    private String culture;

    /**
     * 健康状态
     */
    @ExcelField(value = "健康状态", required = true,
            comment = "提示：健康状态必填" )
    private String health;

    /**
     * 原政治面貌
     */
    @ExcelField(value = "原政治面貌", required = true,
            comment = "提示：原政治面貌必填" )
    private String formerPolitics;

    /**
     * 婚姻状况
     */
    @ExcelField(value = "婚姻状况", required = true,
            comment = "提示：原政治面貌婚姻状况必填" )
    private String maritalStatus;

    /**
     * 居住地
     */
    @ExcelField(value = "居住地", required = true,
            comment = "提示：居住地必填" )
    private String place;

    /**
     * 居住地联系电话
     */
    @ExcelField(value = "居住地联系电话", required = true, regularExp = "^([1]\\d{10}|([\\(（]?0[0-9]{2,3}[）\\)]?[-]?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)$",
            comment = "提示：居住地联系电话格式错误" )
    private String placePhone;

    /**
     * 户籍地
     */
    @ExcelField(value = "户籍地", required = true,
            comment = "提示：户籍地必填" )
    private String domicilePlace;

    /**
     * 户籍地联系电话
     */
    @ExcelField(value = "户籍地联系电话", required = true, regularExp = "^([1]\\d{10}|([\\(（]?0[0-9]{2,3}[）\\)]?[-]?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)$",
            comment = "提示：户籍地联系电话格式错误" )
    private String domicilePhone;

    /**
     * 所在工作单位（学校）
     */
    @ExcelField(value = "所在工作单位", required = true,
            comment = "提示：所在工作单位必填" )
    private String unit;

    /**
     * 所在工作单位（学校）联系电话
     */
    @ExcelField(value = "所在工作单位（学校）联系电话", required = true, regularExp = "^([1]\\d{10}|([\\(（]?0[0-9]{2,3}[）\\)]?[-]?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)$",
            comment = "提示：所在工作单位（学校）联系电话格式错误" )
    private String unitPhone;

    /**
     * 罪名
     */
    @ExcelField(value = "罪名", required = true,
            comment = "提示：罪名必填" )
    private String accusation;

    /**
     * 行种 1管制 2缓刑 3假释 4监外执行
     */
    @ExcelField(value = "行种", required = true,
            comment = "提示：行种必填" )
    private String punishmentType;

    /**
     * 原判刑期
     */
    @ExcelField(value = "原判刑期", required = true,
            comment = "提示：原判刑期必填" )
    private String sentence;

    /**
     * 社区矫正决定机关
     */
    @ExcelField(value = "社区矫正决定机关", required = true,
            comment = "提示：社区矫正决定机关必填" )
    private String decisionUnit;

    /**
     * 原羁押场所
     */
    @ExcelField(value = "原羁押场所", required = true,
            comment = "提示：原羁押场所必填" )
    private String detainSite;

    /**
     * 禁止令内容
     */
    @ExcelField(value = "禁止令内容", required = true,
            comment = "提示：禁止令内容必填" )
    private String injunctionContent;

    /**
     * 禁止期限开始日期
     */
    @ExcelField(value = "禁止期限开始日期", required = true, regularExp = "\\d{4}-\\d{2}-\\d{2}",
            comment = "提示：禁止期限开始日期必填，出生年月日格式为：2020-01-01" )
    private String banBegin;

    /**
     * 禁止期限结束日期
     */
    @ExcelField(value = "禁止期限结束日期", required = true, regularExp = "\\d{4}-\\d{2}-\\d{2}",
            comment = "提示：禁止期限结束日期必填，出生年月日格式为：2020-01-01" )
    private String banEnd;

    /**
     * 矫正类别 A 宽管类：严格遵守社区矫正规定，自我控制力和现实表现良好；B普管类：基本能遵守社区矫正规定，自我控制力和现实表现一般； C严管类：不能遵守社区矫正规定，主观恶性及社会危害性较大，心理不健康，自我控制力和现实表现较差； D初入矫的社区矫正对象； E被剥夺政治权利的社区矫正对象
     */
    @ExcelField(value = "矫正类别", required = true,
            comment = "提示：矫正类别必填" )
    private String correctType;

    /**
     * 矫正期限
     */
    @ExcelField(value = "矫正期限", required = true,
            comment = "提示：矫正期限必填" )
    private String correctDeadline;

    /**
     * 矫正开始日期
     */
    @ExcelField(value = "矫正开始日期日期", required = true, regularExp = "\\d{4}-\\d{2}-\\d{2}",
            comment = "提示：矫正开始日期必填，出生年月日格式为：2020-01-01" )
    private String correctBegin;

    /**
     * 矫正结束日期
     */
    @ExcelField(value = "矫正结束日期", required = true, regularExp = "\\d{4}-\\d{2}-\\d{2}",
            comment = "提示：矫正结束日期必填，出生年月日格式为：2020-01-01" )
    private String correctEnd;

    /**
     * 法律文书收到时间
     */
    @ExcelField(value = "法律文书收到时间", required = true, regularExp = "\\d{4}-\\d{2}-\\d{2}",
            comment = "提示：法律文书收到时间必填，出生年月日格式为：2020-01-01" )
    private String lagalReceipttime;

    /**
     * 报到时间
     */
    @ExcelField(value = "报到时间时间", required = true, regularExp = "\\d{4}-\\d{2}-\\d{2}",
            comment = "提示：报到时间必填，出生年月日格式为：2020-01-01" )
    private String reportingTime;

    /**
     * 报到类型 在规定时限内报到 超出规定时限报到 未报到且下落不明
     */
    @ExcelField(value = "报到类型", required = true,
            comment = "提示：报到类型必填" )
    private String reportingType;

    /**
     * 主要犯罪事实
     */
    @ExcelField(value = "主要犯罪事实", required = true,
            comment = "提示：主要犯罪事实必填" )
    private String corpusDelicti;

    /**
     * 本次犯罪前的违法犯罪记录
     */
    @ExcelField(value = "本次犯罪前的违法犯罪记录", required = true,
            comment = "提示：本次犯罪前的违法犯罪记录必填" )
    private String criminalRecord;

    /**
     * 个人简历 ##隔开
     */
    @ExcelField(value = "个人简历", required = true,
            comment = "提示：个人简历必填" )
    private String resume;

    /**
     * 家庭成员及主要社会关系 ##隔开
     */
    @ExcelField(value = "家庭成员及主要社会关系", required = true,
            comment = "提示：家庭成员及主要社会关系必填" )
    private String familyMember;

    /**
     * 备注
     */
    @ExcelField
    private String remark;
}