package advisor.nutrition.nutritionadvisor.api.ndb;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import advisor.nutrition.nutritionadvisor.api.Callback;
import advisor.nutrition.nutritionadvisor.data.Food;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class NdbApiTest {

    @Test
    public void ndb_search_response_max_received() throws Exception {
        final CountDownLatch responseReceived = new CountDownLatch(1);
        NdbFactory.getNdbApi().searchFood("butter", new Callback<Food[]>() {
            @Override
            public void response(Food[] response) {
                responseReceived.countDown();
                assertThat("response_received", response.length, equalTo(10));
            }
        });
        if (!responseReceived.await(15, TimeUnit.SECONDS)){
            fail();
        }
    }
}
