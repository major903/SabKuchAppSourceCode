package vedam.subkuch.uicomponent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import vedam.subkuch.R;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DateSetListener dateSetListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dateSetListener = (DateSetListener) getActivity();
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.AlertDialogTheme, this, year, month, day);
        long millis = System.currentTimeMillis() - 378683112000L;
        dialog.getDatePicker().setMaxDate(millis);
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (dateSetListener != null)
            dateSetListener.onDateSet(view, year, month, day);
    }

    public interface DateSetListener {
        void onDateSet(DatePicker view, int year, int month, int day);
    }

}
