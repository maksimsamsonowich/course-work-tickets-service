package eu.senla.eventoservice.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@JsonAutoDetect
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ExceptionCaughtDto {

    @JsonProperty(value = "statusId")
    private Integer statusId;

    @JsonProperty(value = "timespan")
    private String timespan;

    @JsonProperty(value = "message")
    private String message;

}
