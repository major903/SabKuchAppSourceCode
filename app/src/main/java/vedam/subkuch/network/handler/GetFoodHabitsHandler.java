package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetFoodHabitsBean;

public interface GetFoodHabitsHandler  extends BaseHandler {
    void onSuccess(GetFoodHabitsBean response);
}
