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
public class UpdateUserPhoneNumberRequestDto {

    @JsonProperty(value = "phoneNumber")
    private String phoneNumber;

    @JsonProperty(value = "phoneNumberConfirmation")
    private String phoneNumberConfirmation;

}
