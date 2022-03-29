package com.bashirmanafikhi.whatsappvideostatuses.Adapters;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirmanafikhi.whatsappvideostatuses.R;

public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


  private Context ctx;
  private String[] List_Items;

  public NotesAdapter(Context context, String[] myData) {
    this.List_Items = myData;
    this.ctx = context;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(ctx).inflate(R.layout.card_note, parent, false);
    return new NoteViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    final NoteViewHolder myHolder = (NoteViewHolder) holder;
    myHolder.txrNote.setText(List_Items[position]);
  }

  @Override
  public int getItemCount() {
    return List_Items.length;
  }



  public class NoteViewHolder extends RecyclerView.ViewHolder{
    TextView txrNote;

    NoteViewHolder(View itemView) {
      super(itemView);
      txrNote = itemView.findViewById(R.id.txtNote);
    }
  }
}
