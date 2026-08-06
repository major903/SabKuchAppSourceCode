package vedam.subkuch.ui.events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentEventBinding;
import vedam.subkuch.network.RegistrationApiClient;
import vedam.subkuch.network.models.learn.LearnCategory;
import vedam.subkuch.network.models.learn.LearnCourse;
import vedam.subkuch.network.models.learn.LearnHomeData;
import vedam.subkuch.network.models.learn.LearnHomeResponse;
import vedam.subkuch.network.models.learn.MyCoursesResponse;

public class EventFragment extends BaseFragment {

    private static final int MY_COURSES_CATEGORY_ID = -1;

    private FragmentEventBinding fragmentEventBinding;
    private final ArrayList<LearnCourse> allCourses = new ArrayList<>();
    private final ArrayList<TextView> categoryChips = new ArrayList<>();
    private LearnCourseAdapter courseAdapter;

    public static EventFragment newInstance() {
        return new EventFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentEventBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_event, container, false);
        return fragmentEventBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        courseAdapter = new LearnCourseAdapter(requireContext(), this::openPurchasePage);
        fragmentEventBinding.lvLearnCourses.setAdapter(courseAdapter);
        loadLearnHome();
    }

    private void loadLearnHome() {
        showLoading(true);
        RegistrationApiClient.getApi().getLearnHome().enqueue(new Callback<LearnHomeResponse>() {
            @Override
            public void onResponse(Call<LearnHomeResponse> call, Response<LearnHomeResponse> response) {
                if (!isAdded()) return;
                showLoading(false);
                LearnHomeResponse value = response.isSuccessful() ? response.body() : null;
                LearnHomeData data = value == null ? null : value.getReturnData();
                if (data == null || data.getCourses() == null) {
                    showEmpty(getString(R.string.learn_load_error));
                    return;
                }
                allCourses.clear();
                allCourses.addAll(data.getCourses());
                createCategoryFilters(data.getCategories());
                showCourses(allCourses);
            }

            @Override
            public void onFailure(Call<LearnHomeResponse> call, Throwable throwable) {
                if (!isAdded()) return;
                showLoading(false);
                showEmpty(getString(R.string.learn_load_error));
            }
        });
    }

    private void createCategoryFilters(ArrayList<LearnCategory> categories) {
        fragmentEventBinding.llLearnCategories.removeAllViews();
        categoryChips.clear();
        addCategoryChip(getString(R.string.learn_all), 0, true);
        addCategoryChip(getString(R.string.learn_my_courses), MY_COURSES_CATEGORY_ID, false);
        if (categories == null) return;
        for (LearnCategory category : categories) {
            addCategoryChip(category.getName(), category.getCourseCategoryId(), false);
        }
    }

    private void addCategoryChip(String title, int categoryId, boolean selected) {
        TextView chip = new TextView(requireContext());
        chip.setText(title);
        chip.setGravity(Gravity.CENTER);
        chip.setTextSize(14);
        chip.setPadding(dp(18), 0, dp(18), 0);
        android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(42));
        params.setMargins(0, 0, dp(8), 0);
        chip.setLayoutParams(params);
        setChipSelected(chip, selected);
        chip.setOnClickListener(view -> {
            for (TextView item : categoryChips) setChipSelected(item, item == chip);
            filterCourses(categoryId);
        });
        categoryChips.add(chip);
        fragmentEventBinding.llLearnCategories.addView(chip);
    }

    private void setChipSelected(TextView chip, boolean selected) {
        chip.setBackgroundResource(selected ? R.drawable.bg_learn_chip_selected : R.drawable.bg_learn_chip_unselected);
        chip.setTextColor(ContextCompat.getColor(requireContext(), selected ? R.color.white : R.color.brand_blue));
        chip.setTypeface(null, selected ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private void filterCourses(int categoryId) {
        if (categoryId == MY_COURSES_CATEGORY_ID) {
            loadMyCourses();
            return;
        }
        if (categoryId == 0) {
            showCourses(allCourses);
            return;
        }
        ArrayList<LearnCourse> filtered = new ArrayList<>();
        for (LearnCourse course : allCourses) {
            if (course.getCourseCategoryId() == categoryId) filtered.add(course);
        }
        showCourses(filtered);
    }

    private void loadMyCourses() {
        showLoading(true);
        RegistrationApiClient.getApi().getMyCourses().enqueue(new Callback<MyCoursesResponse>() {
            @Override
            public void onResponse(Call<MyCoursesResponse> call, Response<MyCoursesResponse> response) {
                if (!isAdded()) return;
                showLoading(false);
                MyCoursesResponse value = response.isSuccessful() ? response.body() : null;
                ArrayList<LearnCourse> courses = value == null ? null : value.getReturnData();
                showCourses(courses == null ? new ArrayList<>() : courses);
            }

            @Override
            public void onFailure(Call<MyCoursesResponse> call, Throwable throwable) {
                if (!isAdded()) return;
                showLoading(false);
                showEmpty(getString(R.string.learn_load_error));
            }
        });
    }

    private void openPurchasePage(LearnCourse course) {
        String purchaseUrl = course.getPurchaseUrl();
        if (purchaseUrl == null || purchaseUrl.trim().isEmpty()) {
            purchaseUrl = "https://www.vedam-it.com/sabkuch-new/learn.html#";
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(purchaseUrl)));
        } catch (Exception exception) {
            Toast.makeText(requireContext(), R.string.learn_purchase_link_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    private void showCourses(ArrayList<LearnCourse> courses) {
        courseAdapter.setCourses(courses);
        fragmentEventBinding.lvLearnCourses.setVisibility(courses.isEmpty() ? View.GONE : View.VISIBLE);
        showEmpty(courses.isEmpty() ? getString(R.string.learn_no_courses) : null);
    }

    private void showLoading(boolean loading) {
        fragmentEventBinding.progressLearn.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void showEmpty(String message) {
        boolean visible = message != null;
        fragmentEventBinding.tvLearnEmpty.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (visible) fragmentEventBinding.tvLearnEmpty.setText(message);
    }
}
