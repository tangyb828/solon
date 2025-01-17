package features;

import features.nami.GitHub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.nami.Nami;
import org.noear.nami.annotation.Body;
import org.noear.nami.annotation.NamiClient;
import org.noear.nami.coder.snack3.SnackDecoder;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import webapp.models.UserModel;

/**
 * @author noear 2021/1/5 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
@SolonTest(webapp.TestApp.class)
public class HttpParam3Test {
    @Test
    public void nami(){
        ParamService paramService = Nami.builder()
                .decoder(SnackDecoder.instance)
                .create(ParamService.class);

        UserModel userModel = new UserModel();
        userModel.name = "noear";

        UserModel model2 = paramService.model(userModel);

        assert userModel.name.equals(model2.name);
    }


    @NamiClient(value = "local:/demo2/param", upstream = "http://localhost:8080")
    public interface ParamService{
        UserModel model(@Body UserModel model);
    }
}
