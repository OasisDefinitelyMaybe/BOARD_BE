package org.choongang.api.members;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.api.commons.JSONData;
import org.choongang.api.members.dto.RequestLogin;
import org.choongang.api.members.dto.ResponseLogin;
import org.choongang.configs.jwt.CustomJwtFilter;
import org.choongang.models.member.MemberInfoService;
import org.choongang.models.member.MemberJoinService;
import org.choongang.models.member.MemberLoginService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberLoginService loginService;
    private final MemberInfoService infoService;
    private final MemberJoinService joinService;

    /**
     * accessToken 발급
     *
     */
    @PostMapping("/token")
    public ResponseEntity<JSONData<ResponseLogin>> authorize(@Valid @RequestBody RequestLogin requestLogin, Errors errors) {
        if (errors.hasErrors()) {

        }

        ResponseLogin token = loginService.authenticate(requestLogin.email(), requestLogin.password());

        HttpHeaders headers = new HttpHeaders();
        headers.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer " + token.accessToken());

        JSONData<ResponseLogin> data = new JSONData<>(token);

        return ResponseEntity.status(data.getStatus())
                .headers(headers)
                .body(data);
    }
}
