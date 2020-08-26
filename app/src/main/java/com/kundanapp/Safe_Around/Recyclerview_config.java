package com.kundanapp.Safe_Around;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Recyclerview_config {
    private Context mContext;
    private BookAdapter mBooksAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<volunteerinfo> books, List<String> keys)
    {
        mContext = context;
        mBooksAdapter = new BookAdapter(books,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mBooksAdapter);
    }

    class BookItemView extends RecyclerView.ViewHolder{
        private TextView mname;
        private TextView mstate;
        private TextView mcity;
        private TextView mphone;
        private TextView mdesc;

        private String key;

        public BookItemView(ViewGroup parent)
        {
            super(LayoutInflater.from(mContext)
                    .inflate(R.layout.volunteerscontent, parent, false));
            mname = (TextView) itemView.findViewById(R.id.orgname);
            mphone = (TextView) itemView.findViewById(R.id.phonename);
            mstate = (TextView) itemView.findViewById(R.id.statename);
            mcity = (TextView) itemView.findViewById(R.id.cityname);
            mdesc = (TextView) itemView.findViewById(R.id.descname);
        }
        public void bind(volunteerinfo book, String key)
        {
            mstate.setText(book.getState());
            mcity.setText(book.getCity());
            mname.setText(book.getName());
            mphone.setText(book.getPhone());
            mdesc.setText(book.getDesc());
            this.key=key;
        }
    }
    class BookAdapter extends RecyclerView.Adapter<BookItemView>{
        private List<volunteerinfo> mbooklist;
        private List<String> mkeys;

        public BookAdapter(List<volunteerinfo> mbooklist, List<String> mkeys) {
            this.mbooklist = mbooklist;
            this.mkeys = mkeys;
        }

        @NonNull
        @Override
        public BookItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookItemView holder, int position) {
            holder.bind(mbooklist.get(position),mkeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mbooklist.size();
        }
    }
}
