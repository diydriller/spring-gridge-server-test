package com.gridge.server.service.sns.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoAccountInfo {
    private Account kakao_account;

    @Getter
    public static class Account {
        private String email;
        private String birthyear;
        private String birthday;
        private String phone_number;
        private String name;
        private Profile profile;
    }

    @Getter
    public static class Profile {
        private String nickname;
        private String profile_image_url;
    }
}
