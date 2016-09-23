package us.rzweb.taotao.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertieService {

    @Value("${TAOTAO_SSO_URL}")
    public String TAOTAO_SSO_URL;

}
