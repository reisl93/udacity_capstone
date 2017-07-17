package advisor.nutrition.nutritionadvisor.ui.overview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import advisor.nutrition.nutritionadvisor.R;


class UserSpinnerAdapter extends ArrayAdapter<String> implements SpinnerAdapter{

    UserSpinnerAdapter(@NonNull Context context) {
        super(context, R.layout.spinner_users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.spinner_users, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.tv_user_name)).setText(user);

        return convertView;
    }
}
