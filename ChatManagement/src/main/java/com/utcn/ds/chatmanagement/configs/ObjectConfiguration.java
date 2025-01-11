package com.utcn.ds.chatmanagement.configs;

import com.utcn.ds.chatmanagement.controller.dto.MessageDTO;
import com.utcn.ds.chatmanagement.entity.Message;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ObjectConfiguration {

    private final RestTemplateBuilder restTemplateBuilder;

    public ObjectConfiguration(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Message.class, MessageDTO.class)
                .addMapping(src -> src.getSession().getId(), MessageDTO::setSession);


        return modelMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return restTemplateBuilder.build();
    }
}