package com.example.ms1.note.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

    @NotEmpty(message = "사용하실 ID를 입력해주세요")
    @Size(min = 5, max = 20, message = "5~20자 내로 설정해주세요")
    private String username;

    @NotEmpty(message = "사용하실 비밀번호를 입력해주세요")
    private String password1;

    @NotEmpty(message = "비밀번호를 확인해주세요.")
    private String password2;

    @NotEmpty(message = "사용하실 이메일을 입력해주세요.")
    @Email
    private String email;
}
