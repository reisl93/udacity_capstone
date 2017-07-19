package advisor.nutrition.nutritionadvisor.api.ndb;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import advisor.nutrition.nutritionadvisor.data.Food;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class NdbApiTest {

    @Test
    public void ndb_search_response_max_received() throws Exception {
        final CountDownLatch responseReceived = new CountDownLatch(1);
        NdbFactory.getNdbApi().searchFood("butter", new Callback<Food[]>() {
            @Override
            public void onResponse(@NonNull Call<Food[]> call, @NonNull Response<Food[]> response) {
                responseReceived.countDown();
                assertThat("response_received", response.body().length, equalTo(10));
            }

            @Override
            public void onFailure(@NonNull Call<Food[]> call, @NonNull Throwable t) {

            }
        });
        if (!responseReceived.await(15, TimeUnit.SECONDS)){
            fail();
        }
    }
}
