package collabalist.crash_let;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by deepak on 8/10/18.
 */

public class ParamsAdapter extends RecyclerView.Adapter {
    Context contextd;
    ArrayList<ItemParams> list;

    public ParamsAdapter(Context contextd, ArrayList<ItemParams> list) {
        this.contextd = contextd;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View header = LayoutInflater.from(contextd).inflate(R.layout.item_header, parent, false);
        View param = LayoutInflater.from(contextd).inflate(R.layout.item_param, parent, false);
        return viewType == 1
                ? new HeaderHolder(header)
                : new ParamHolder(param);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).title.setText(list.get(position).getKey());
        } else if (holder instanceof ParamHolder) {
            ((ParamHolder) holder).keyTxt.setText(list.get(position).getKey());
            ((ParamHolder) holder).valueTxt.setText(list.get(position).getValue());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType().equals("title") ? 1 : 2;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView title;

        public HeaderHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.headerTitle);
        }
    }

    class ParamHolder extends RecyclerView.ViewHolder {
        TextView keyTxt, valueTxt;

        public ParamHolder(View itemView) {
            super(itemView);
            keyTxt = (TextView) itemView.findViewById(R.id.keyTxt);
            valueTxt = (TextView) itemView.findViewById(R.id.valueTxt);
        }
    }
}
