package com.limbae.pfy.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    Long uid;

    String username;

    String password;

    String site;

    String name;

    String phone;

    String img;

}
