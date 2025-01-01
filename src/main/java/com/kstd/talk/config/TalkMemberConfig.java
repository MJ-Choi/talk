package com.kstd.talk.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "talk.member")
public class TalkMemberConfig {
    private int popularDay = 3;
    private int openDay = 7;
    private int closeDay = 1;
    private int idLength = 5;
}
