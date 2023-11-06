package core.di.factory.example;

import core.annotation.Controller;
import core.annotation.Inject;

@Controller
public class MyUserController {
    private MyUserService myUserService;

    public MyUserController() {
    }

    @Inject
    public void setMyUserService(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    public MyUserService getMyUserService() {
        return myUserService;
    }
}
