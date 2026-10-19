package com.codingshuttle.testingApp.TestingApp.services.impl;


import com.codingshuttle.testingApp.TestingApp.services.DataService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DataServiceDevImpl implements DataService {
    @Override
    public String getData() {
        return "dev data";
    }
}
