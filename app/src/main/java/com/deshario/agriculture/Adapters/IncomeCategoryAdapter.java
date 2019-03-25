package com.deshario.agriculture.Adapters;
        import android.animation.ObjectAnimator;
        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
        import com.deshario.agriculture.Models.Category;
        import com.deshario.agriculture.Models.Records;
        import com.deshario.agriculture.R;

        import java.util.List;

public class IncomeCategoryAdapter extends RecyclerView.Adapter<IncomeCategoryAdapter.DataObjectHolder> {
    private Context context;

    public IncomeCategoryAdapter() {
    }

    private List<Records> mDataset;
    private List<Double> mpercent;
    private List<Integer> mColors;
    private String thb = "\u0E3F";
    private int catg_type = 0;

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_icon;
        TextView item_name;
        TextView item_amount;
        RoundCornerProgressBar roundCornerProgressBar;

        public DataObjectHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            item_icon = (ImageView) itemView.findViewById(R.id.icon);
            item_name = (TextView) itemView.findViewById(R.id.itemname);
            item_amount = (TextView) itemView.findViewById(R.id.amount);
            roundCornerProgressBar = (RoundCornerProgressBar) itemView.findViewById(R.id.progressbar);
            roundCornerProgressBar.setMax(100);
        }

        @Override
        public void onClick(View v) {
            Records record  = getItem(getPosition());
            //paydebt(record);
        }
    }

    public IncomeCategoryAdapter(List<Records> myDataset, List<Integer> colors, int type, List<Double> percents, Context context) {
        mDataset = myDataset;
        mpercent = percents;
        mColors = colors;
        catg_type = type;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_category_adaplist, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.item_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.coins));
        holder.item_name.setText(mDataset.get(position).getCategory().getCat_item()+" ("+mpercent.get(position)+"%)");
        holder.item_amount.setText(thb+mDataset.get(position).getData_amount());
        if(catg_type == Category.CATEGORY_INCOME)
            holder.roundCornerProgressBar.setProgressBackgroundColor(context.getResources().getColor(R.color.primary_bootstrap));
        if(catg_type == Category.CATEGORY_EXPENSE)
            holder.roundCornerProgressBar.setProgressBackgroundColor(context.getResources().getColor(R.color.orange));

        fillcolor(holder.roundCornerProgressBar,mColors.get(position));
        animateProgress(holder.roundCornerProgressBar,mpercent.get(position).floatValue());
        //holder.roundCornerProgressBar.setProgress(mpercent.get(position).floatValue());
    }

    public Records getItem(int position) {
        return mDataset.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public void animateProgress(final RoundCornerProgressBar progressBar, float toProgress) {
        ObjectAnimator progressAnimator;
        progressAnimator = ObjectAnimator.ofFloat(progressBar, "progress", 0.0f,toProgress);
        progressAnimator.setDuration(1000);
        progressAnimator.start();
    }

    public void fillcolor(RoundCornerProgressBar progressBar, int color){
        progressBar.setProgressColor(color);
    }

}