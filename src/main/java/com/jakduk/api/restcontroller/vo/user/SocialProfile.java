package com.jakduk.api.restcontroller.vo.user;

/**
 * access token으로 유저 프로필을 조회하여 담아오는 객체
 *
 * @author pyohwan
 *         16. 7. 31 오후 8:34
 */

public class SocialProfile {

    private String id;
    private String nickname;
    private String email;
    private String smallPictureUrl;
    private String largePictureUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSmallPictureUrl() {
        return smallPictureUrl;
    }

    public void setSmallPictureUrl(String smallPictureUrl) {
        this.smallPictureUrl = smallPictureUrl;
    }

    public String getLargePictureUrl() {
        return largePictureUrl;
    }

    public void setLargePictureUrl(String largePictureUrl) {
        this.largePictureUrl = largePictureUrl;
    }

    @Override
    public String toString() {
        return "SocialProfile{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", smallPictureUrl='" + smallPictureUrl + '\'' +
                ", largePictureUrl='" + largePictureUrl + '\'' +
                '}';
    }
}
