package cc.mrbird.febs.common.core.entity.system;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TransmitFile {
	@ApiModelProperty("附件带的id")
	private Integer Id;
	@ApiModelProperty("附件名字")
	private String fileName;
	@ApiModelProperty("附件目录路径")
	private String filePath;
	@ApiModelProperty("附件详细路径")
	private String fileUrl;
}
