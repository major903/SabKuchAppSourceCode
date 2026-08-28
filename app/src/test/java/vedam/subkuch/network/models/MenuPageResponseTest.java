package vedam.subkuch.network.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import vedam.subkuch.utils.MenuCache;

public class MenuPageResponseTest {

    @Test
    public void parsesAuthenticatedPaginatedMenuResponse() {
        String json = "{\"ReturnCode\":1,\"ReturnMessage\":\"success\",\"ReturnData\":{"
                + "\"TotalCount\":4,\"PageIndex\":1,\"PageSize\":20,\"Menus\":["
                + "{\"MenuId\":2,\"Menu\":\"Edit Profile\",\"CityId\":1,\"SortOrder\":1},"
                + "{\"MenuId\":3,\"Menu\":\"My Wallet\",\"CityId\":1,\"SortOrder\":2},"
                + "{\"MenuId\":4,\"Menu\":\"Contribute\",\"CityId\":1,\"SortOrder\":3},"
                + "{\"MenuId\":5,\"Menu\":\"Inbox\",\"CityId\":1,\"SortOrder\":4}]}}";
        Type type = new TypeToken<BaseResponse<MenuPage>>() { }.getType();

        BaseResponse<MenuPage> response = new Gson().fromJson(json, type);

        assertEquals(1, response.getReturnCode());
        assertNotNull(response.getReturnData());
        assertEquals(4, response.getReturnData().getTotalCount());
        assertEquals(4, response.getReturnData().getMenus().size());
        assertEquals(MenuIds.EDIT_PROFILE, response.getReturnData().getMenus().get(0).getMenuId());
        assertEquals("Edit Profile", response.getReturnData().getMenus().get(0).getName());
        assertEquals(MenuIds.WALLET, response.getReturnData().getMenus().get(1).getMenuId());
        assertEquals("My Wallet", response.getReturnData().getMenus().get(1).getName());
    }

    @Test
    public void cachedMenusUseStableBackendSortOrder() {
        OMenu inbox = new OMenu(MenuIds.INBOX, null, "Inbox", "4");
        OMenu profile = new OMenu(MenuIds.EDIT_PROFILE, null, "Edit Profile", "1");
        OMenu wallet = new OMenu(MenuIds.WALLET, null, "My Wallet", "2");
        List<OMenu> menus = MenuCache.INSTANCE.stableOrder(Arrays.asList(inbox, wallet, profile));

        assertEquals(MenuIds.EDIT_PROFILE, menus.get(0).getMenuId());
        assertEquals(MenuIds.WALLET, menus.get(1).getMenuId());
        assertEquals(MenuIds.INBOX, menus.get(2).getMenuId());
    }

    @Test
    public void visibleMenuComparisonIgnoresNonVisualCityChanges() {
        List<OMenu> rendered = Arrays.asList(
                new OMenu(MenuIds.EDIT_PROFILE, "1", "Edit Profile", "1"),
                new OMenu(MenuIds.WALLET, "1", "My Wallet", "2"));
        List<OMenu> refreshed = Arrays.asList(
                new OMenu(MenuIds.WALLET, "2", "My Wallet", "2"),
                new OMenu(MenuIds.EDIT_PROFILE, "2", "Edit Profile", "1"));

        assertTrue(MenuCache.INSTANCE.hasSameVisibleContent(rendered, refreshed));

        refreshed.get(1).setName("Profile");
        assertFalse(MenuCache.INSTANCE.hasSameVisibleContent(rendered, refreshed));
    }
}
