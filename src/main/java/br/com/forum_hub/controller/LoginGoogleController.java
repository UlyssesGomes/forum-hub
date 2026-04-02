package br.com.forum_hub.controller;

import br.com.forum_hub.domain.login.external.AbstractExternalLoginService;
import br.com.forum_hub.domain.login.external.google.ExternalLoginServiceGoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login/google")
public class LoginGoogleController extends AbstractExternalLoginController {

    @Autowired
    private ExternalLoginServiceGoogleService loginGoogleService;

    @Override
    public AbstractExternalLoginService getService() {
        return loginGoogleService;
    }
}
