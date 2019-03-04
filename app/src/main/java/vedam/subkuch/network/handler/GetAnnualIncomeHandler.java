package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetAnnualIncome;

public interface GetAnnualIncomeHandler extends BaseHandler {
    void onSuccess(GetAnnualIncome response);
}
