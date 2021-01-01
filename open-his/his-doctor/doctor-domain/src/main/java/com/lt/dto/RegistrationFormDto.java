package com.lt.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Mr.Tian
 */
@ApiModel(value="com-lt-dto-RegistrationFormDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationFormDto implements Serializable {

    private PatientDto patientDto;

    private RegistrationDto registrationDto;
}
