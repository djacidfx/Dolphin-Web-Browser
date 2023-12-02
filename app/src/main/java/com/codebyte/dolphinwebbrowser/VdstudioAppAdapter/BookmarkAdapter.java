package com.codebyte.dolphinwebbrowser.VdstudioAppAdapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.codebyte.dolphinwebbrowser.VdstudioAppActivity.BrowserActivity;
import com.codebyte.dolphinwebbrowser.VdstudioAppActivity.IncognitoActivity;
import com.codebyte.dolphinwebbrowser.VdstudioAppDatabase.Database;
import com.codebyte.dolphinwebbrowser.VdstudioAppFragment.BookmarkFragment;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.BookmarkData;

import java.util.ArrayList;

import dolphinwebbrowser.R;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {
    public final Database database;
    public Dialog bookmarkDialog;
    FragmentActivity activity;
    ArrayList<BookmarkData> bookmarkData;
    LayoutInflater inflater;
    int typeMode;

    public BookmarkAdapter(FragmentActivity fragmentActivity, ArrayList<BookmarkData> arrayList, int i) {
        this.activity = fragmentActivity;
        this.bookmarkData = arrayList;
        this.database = new Database(fragmentActivity);
        this.typeMode = i;
        this.inflater = LayoutInflater.from(fragmentActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.item_bookmark, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.bookmark_title.setText(this.bookmarkData.get(i).getName());
        viewHolder.bookmark_url.setText(this.bookmarkData.get(i).getUrl());
        if (!this.bookmarkData.get(i).getImage().equals("null")) {
            try {
                byte[] decode = Base64.decode(this.bookmarkData.get(i).getImage(), 0);
                viewHolder.bookmark_icon.setImageBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length));
            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            viewHolder.bookmark_icon.setImageDrawable(this.activity.getResources().getDrawable(R.drawable.iv_web_iicon));
        }
        viewHolder.bookmark_optionmenu.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(BookmarkAdapter.this.activity, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_bookmark, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                    @SuppressLint("ResourceType")
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_book_delete:
                                BookmarkAdapter.this.database.deleteBookmark(BookmarkAdapter.this.bookmarkData.get(i).getId());
                                BookmarkAdapter.this.bookmarkData.remove(i);
                                if (BookmarkAdapter.this.bookmarkData.size() == 0) {
                                    BookmarkFragment.recycleBookmark.setVisibility(View.GONE);
                                    BookmarkFragment.noBookmark.setVisibility(View.VISIBLE);
                                } else {
                                    BookmarkFragment.recycleBookmark.setVisibility(View.VISIBLE);
                                    BookmarkFragment.noBookmark.setVisibility(View.GONE);
                                }
                                BookmarkAdapter.this.notifyDataSetChanged();
                                return true;
                            case R.id.menu_book_edit:
                                BookmarkAdapter.this.bookmarkDialog = new Dialog(BookmarkAdapter.this.activity, R.style.WideDialog);
                                BookmarkAdapter.this.bookmarkDialog.setContentView(R.layout.dialog_bookmark);
                                BookmarkAdapter.this.bookmarkDialog.getWindow().setBackgroundDrawableResource(17170445);
                                final EditText editText = (EditText) BookmarkAdapter.this.bookmarkDialog.findViewById(R.id.bookmark_edit_title);
                                final EditText editText2 = (EditText) BookmarkAdapter.this.bookmarkDialog.findViewById(R.id.bookmark_edit_url);
                                editText.setText(BookmarkAdapter.this.bookmarkData.get(i).getName());
                                editText2.setText(BookmarkAdapter.this.bookmarkData.get(i).getUrl());
                                ((Button) BookmarkAdapter.this.bookmarkDialog.findViewById(R.id.bookmark_cancel)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        BookmarkAdapter.this.bookmarkDialog.dismiss();
                                    }
                                });
                                ((Button) BookmarkAdapter.this.bookmarkDialog.findViewById(R.id.bookmark_done)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        if (editText.getText().toString().trim().length() <= 0) {
                                            Toast.makeText(BookmarkAdapter.this.activity, "Enter Title...", Toast.LENGTH_SHORT).show();
                                        } else if (editText2.getText().toString().trim().length() > 0) {
                                            BookmarkData bookmark_Data = new BookmarkData();
                                            bookmark_Data.setId(BookmarkAdapter.this.bookmarkData.get(i).getId());
                                            bookmark_Data.setName(editText.getText().toString().trim());
                                            bookmark_Data.setUrl(editText2.getText().toString().trim());
                                            bookmark_Data.setImage(BookmarkAdapter.this.bookmarkData.get(i).getImage());
                                            BookmarkAdapter.this.bookmarkData.get(i).setName(editText.getText().toString().trim());
                                            BookmarkAdapter.this.bookmarkData.get(i).setUrl(editText2.getText().toString().trim());
                                            BookmarkAdapter.this.database.updateBookmark(bookmark_Data);
                                            BookmarkAdapter.this.notifyItemChanged(i);
                                            BookmarkAdapter.this.bookmarkDialog.dismiss();
                                        } else {
                                            Toast.makeText(BookmarkAdapter.this.activity, "Enter Url...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                BookmarkAdapter.this.bookmarkDialog.show();
                                BookmarkAdapter.this.bookmarkDialog.setCancelable(false);
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.bookmarkData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookmark_icon;
        ImageView bookmark_optionmenu;
        TextView bookmark_title;
        TextView bookmark_url;

        public ViewHolder(View view) {
            super(view);
            this.bookmark_icon = (ImageView) view.findViewById(R.id.bookmark_image);
            this.bookmark_optionmenu = (ImageView) view.findViewById(R.id.bookmark_optionmenu);
            this.bookmark_title = (TextView) view.findViewById(R.id.bookmark_title);
            this.bookmark_url = (TextView) view.findViewById(R.id.bookmark_url);

            view.setOnClickListener(view1 -> {
                if (BookmarkAdapter.this.typeMode == 0) {
                    BrowserActivity.web_view.loadUrl(BookmarkAdapter.this.bookmarkData.get(ViewHolder.this.getAdapterPosition()).getUrl());
                } else {
                    IncognitoActivity.webView.loadUrl(BookmarkAdapter.this.bookmarkData.get(ViewHolder.this.getAdapterPosition()).getUrl());
                }
                BookmarkAdapter.this.activity.onBackPressed();
            });
        }
    }
}
