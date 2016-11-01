package com.pokemonbattlearena.android.fragments.team;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.pokemonbattlearena.android.R;
import com.pokemonbattlearena.android.engine.database.Move;
import com.pokemonbattlearena.android.engine.database.Pokemon;

import java.util.List;

/**
 * Created by Spencer Amann on 10/30/16.
 */

public class MoveAdapter extends BaseAdapter {
    List<Move> mMoveList;

    Context mContext;

    public MoveAdapter(Context c, List<Move> moves) {
        this.mMoveList = moves;
        this.mContext = c;
    }

    @Override
    public int getCount() {
        return mMoveList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMoveList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMoveList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Move move = mMoveList.get(position);
        MoveViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.move_list_item, parent, false);
            holder = new MoveViewHolder();
            holder.moveCheckbox = (CheckBox) convertView.findViewById(R.id.move_checkbox);
            holder.moveText = (TextView) convertView.findViewById(R.id.move_textview);
            convertView.setTag(holder);
        } else {
            holder = (MoveViewHolder) convertView.getTag();
        }
        holder.moveText.setText(move.getName());
        holder.moveCheckbox.setChecked(false);

        return convertView;
    }

    public class MoveViewHolder {
        TextView moveText;
        CheckBox moveCheckbox;
    }
}
