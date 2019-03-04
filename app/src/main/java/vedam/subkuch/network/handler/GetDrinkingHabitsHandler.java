package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetDrinkingHabits;

public interface GetDrinkingHabitsHandler extends BaseHandler {
    void onSuccess(GetDrinkingHabits response);
}
