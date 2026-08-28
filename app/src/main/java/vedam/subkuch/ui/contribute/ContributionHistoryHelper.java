package vedam.subkuch.ui.contribute;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import vedam.subkuch.R;
import vedam.subkuch.network.models.DataEntryListResponse;

/**
 * Shared "unique data entered" history logic for the contribution forms: tolerant
 * Entries-array extraction, a per-user preferences cache, and simple text rows,
 * mirroring the company data-entry form.
 */
public final class ContributionHistoryHelper {

    interface EntryFormatter<T> {
        String format(int position, T item);
    }

    private ContributionHistoryHelper() {
    }

    public static <T> ArrayList<T> extractEntries(DataEntryListResponse response, Class<T> itemClass) {
        ArrayList<T> entries = new ArrayList<>();
        if (response == null || response.getReturnData() == null) return entries;
        JsonArray array = findEntriesArray(response.getReturnData());
        if (array == null) return entries;
        Gson gson = new Gson();
        for (JsonElement item : array) {
            if (item != null && item.isJsonObject()) entries.add(gson.fromJson(item, itemClass));
        }
        return entries;
    }

    private static JsonArray findEntriesArray(JsonElement returnData) {
        if (returnData.isJsonArray()) return returnData.getAsJsonArray();
        if (!returnData.isJsonObject()) return null;
        JsonObject dataObject = returnData.getAsJsonObject();
        String[] listKeys = {"Entries", "entries", "Data", "data", "Items", "items", "Records", "records"};
        for (String key : listKeys) {
            JsonElement value = dataObject.get(key);
            if (value != null && value.isJsonArray()) return value.getAsJsonArray();
        }
        for (java.util.Map.Entry<String, JsonElement> entry : dataObject.entrySet()) {
            if (entry.getValue().isJsonArray()) return entry.getValue().getAsJsonArray();
        }
        return null;
    }

    static <T> ArrayList<T> readCache(Context context, String prefsName, String key, Type type) {
        String json = context.getApplicationContext()
                .getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                .getString(key, null);
        if (json == null) return new ArrayList<>();
        try {
            ArrayList<T> values = new Gson().fromJson(json, type);
            return values == null ? new ArrayList<>() : values;
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }

    static <T> void writeCache(Context context, String prefsName, String key, List<T> entries) {
        context.getApplicationContext()
                .getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                .edit()
                .putString(key, new Gson().toJson(entries))
                .apply();
    }

    static <T> void displayEntries(Context context, LinearLayout container, TextView emptyView,
                                   List<T> entries, EntryFormatter<T> formatter) {
        container.removeAllViews();
        if (entries == null || entries.isEmpty()) {
            emptyView.setText(R.string.no_data_entered);
            emptyView.setVisibility(TextView.VISIBLE);
            return;
        }
        emptyView.setVisibility(TextView.GONE);
        for (int index = 0; index < entries.size(); index++) {
            TextView entryView = new TextView(context);
            entryView.setText(formatter.format(index + 1, entries.get(index)));
            entryView.setTextColor(ContextCompat.getColor(context, R.color.form_text_primary));
            entryView.setTextSize(14);
            entryView.setLineSpacing(0, 1.15f);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (index > 0) params.topMargin =
                    context.getResources().getDimensionPixelSize(R.dimen.margin_8dp);
            entryView.setLayoutParams(params);
            container.addView(entryView);
        }
    }

    /** Formats "1) value, value, value" skipping blanks, like the company data-entry rows. */
    static String formatEntry(int position, String... values) {
        StringBuilder builder = new StringBuilder(position + ") ");
        for (String value : values) {
            if (value == null || value.trim().isEmpty()) continue;
            if (builder.length() > 3) builder.append(", ");
            builder.append(value.trim());
        }
        return builder.toString();
    }
}
