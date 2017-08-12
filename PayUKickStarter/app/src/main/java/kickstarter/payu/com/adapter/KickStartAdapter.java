package kickstarter.payu.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kickstarter.payu.com.domain.KickStartBean;
import kickstarter.payu.com.payukickstarter.R;

public class KickStartAdapter extends ArrayAdapter implements Filterable {

    private int resource;
    private LayoutInflater inflater;

    private List<KickStartBean> beanArrayList;

    public KickStartAdapter(Context ctx, int resourceId, ArrayList<KickStartBean> list) {

        super(ctx, resourceId, list);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        beanArrayList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = (RelativeLayout) inflater.inflate(resource, null);

        KickStartBean bean = (KickStartBean) getItem(position);

        /* Take the TextView from layout and set the title */
        TextView txtTitle = convertView.findViewById(R.id.viewTitle);
        txtTitle.setText((bean.getTitle() == null) ? "" : bean.getTitle());

        /* Take the TextView from layout and set the amount pledged */
        TextView txtAmtPledged = convertView.findViewById(R.id.pledge);
        txtAmtPledged.setText(String.valueOf(bean.getAmtPledged()));

        /* Take the TextView from layout and set the backers */
        TextView txtBackers = convertView.findViewById(R.id.backers);
        txtBackers.setText((bean.getNumBackers() == null) ? "" : bean.getNumBackers());

        /* Take the TextView from layout and set days to go */
        TextView txtDaysToGo = convertView.findViewById(R.id.daysToGo);
        txtDaysToGo.setText((bean.getEndTime() == null) ? "" : bean.getEndTime());

        return convertView;
    }


    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                beanArrayList = (List<KickStartBean>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<KickStartBean> FilteredArrayNames = new ArrayList<KickStartBean>();

                constraint = constraint.toString().toLowerCase();
                KickStartBean bean;

                for (int i = 0; i < beanArrayList.size(); i++) {
                    bean = beanArrayList.get(i);
                    String title = bean.getTitle();
                    if (title.toLowerCase().startsWith(constraint.toString())) {
                        FilteredArrayNames.add(bean);
                    }
                }
                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;

                return results;
            }
        };
    }

}