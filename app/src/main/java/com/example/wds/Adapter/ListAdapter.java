package com.example.wds.Adapter;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.example.wds.Model.DataModel;
import com.example.wds.R;


import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DataModel> dataModelArrayList;

    public ListAdapter(Context context, ArrayList<DataModel> dataModelArrayList) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return dataModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_player, null, true);

           // holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tvname = (TextView) convertView.findViewById(R.id.name);
            holder.tvcountry = (TextView) convertView.findViewById(R.id.country);
            holder.tvcity = (TextView) convertView.findViewById(R.id.city);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

      //  Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.tvname.setText("Name: "+dataModelArrayList.get(position).getName());
        holder.tvcountry.setText("Country: "+dataModelArrayList.get(position).getCountry());
        holder.tvcity.setText("City: "+dataModelArrayList.get(position).getCity());

        AppController.Forte(holder.tvname);
        Color(holder.tvname, position);
        AppController.Forte(holder.tvcountry);
        Color(holder.tvcountry, position);
        AppController.Forte(holder.tvcity);
        Color(holder.tvcity, position);

        return convertView;
    }

    private class ViewHolder {

        protected TextView tvname, tvcountry, tvcity;
        protected ImageView iv;
    }
    private void Color(TextView tv, int position) {

        int mod = position % 3;

        switch (mod) {
            case 0:
                GradientColor(tv, GetColor(R.color.list_color_1), GetColor(R.color.list_color_1a));
                break;
            case 1:
                GradientColor(tv, GetColor(R.color.list_color_2), GetColor(R.color.list_color_2a));
                break;
            case 2:
                GradientColor(tv, GetColor(R.color.list_color_3), GetColor(R.color.list_color_3a));

                break;
            default:
                break;
        }
    }

    private void GradientColor(TextView tv, int color1, int color2) {
        Shader shader = new LinearGradient(
                0, 0, 0, tv.getTextSize(),
                color1, color2, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(shader);
    }

    private int GetColor(int id) {
        return context.getResources().getColor(id);
    }
}
