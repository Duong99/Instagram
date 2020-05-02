package com.example.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.model.HomePost;
import com.example.instagram.model.Person;
import com.example.instagram.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.ViewHolder>{
    private ArrayList<HomePost> homePosts;
    private Context context;

    public HomePostAdapter(ArrayList<HomePost> homePosts, Context context) {
        this.homePosts = homePosts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_recycler_home_post, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtUsername.setText(homePosts.get(position).getPerson().getUsername());

        Picasso.with(context).load(homePosts.get(position).getPerson().getImvPeron())
                .error(R.drawable.ic_launcher_background).into(holder.imvUser);

        Picasso.with(context).load(homePosts.get(position).getPicture().getDisplay_url())
                .error(R.drawable.picture_profile_default).into(holder.imvPersonal);

        if (Integer.parseInt(homePosts.get(position).getPicture().getEdge_media_to_comment()) > 1){
            holder.txtTotalComents.setText(String.format("%s %s",
                    homePosts.get(position).getPicture().getEdge_media_to_comment(),
                    context.getResources().getString(R.string.comments)));
        }else {
            holder.txtTotalComents.setText(String.format("%s %s",
                    homePosts.get(position).getPicture().getEdge_media_to_comment(),
                    context.getResources().getString(R.string.comment)));
        }

        if (Integer.parseInt(homePosts.get(position).getPicture().getEdge_liked_by_count()) > 1){
            holder.txtTotalLike.setText(String.format("%s %s",
                    homePosts.get(position).getPicture().getEdge_liked_by_count(),
                    context.getResources().getString(R.string.likes)));
        }else {
            holder.txtTotalLike.setText(String.format("%s %s",
                    homePosts.get(position).getPicture().getEdge_liked_by_count(),
                    context.getResources().getString(R.string.like)));
        }
    }

    @Override
    public int getItemCount() {
        return homePosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView imvUser;
        private ImageView imvPersonal;
        private TextView txtTotalLike, txtUsername;
        private TextView txtTotalComents;
         private ImageButton txtMoreOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imvPersonal = itemView.findViewById(R.id.imvPostPersonalHomeHP);
            imvUser = itemView.findViewById(R.id.circleImvUserHP);
            txtUsername = itemView.findViewById(R.id.txtUserNameHP);
            txtTotalLike = itemView.findViewById(R.id.txtTotalLikePostPersonalHP);
            txtTotalComents = itemView.findViewById(R.id.txtTotalCommentsHP);
            txtMoreOption = itemView.findViewById(R.id.ibtnMoreOption);

            txtMoreOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion = getAdapterPosition();
                    if (postion != RecyclerView.NO_POSITION){
                        mClickHome.moreOption(homePosts.get(postion).getPicture());
                    }
                }
            });

            txtTotalLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion = getAdapterPosition();
                    if (postion != RecyclerView.NO_POSITION){
                        mClickHome.totalLike(homePosts.get(postion).getPicture().getShortcode());
                    }
                }
            });

            txtUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion = getAdapterPosition();
                    if (postion != RecyclerView.NO_POSITION){
                        mClickHome.profile(homePosts.get(postion).getPerson());
                    }
                }
            });

            imvUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion = getAdapterPosition();
                    if (postion != RecyclerView.NO_POSITION){
                        mClickHome.profile(homePosts.get(postion).getPerson());
                    }
                }
            });

            imvPersonal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion = getAdapterPosition();
                    if (postion != RecyclerView.NO_POSITION){
                        mClickHome.image(homePosts.get(postion).getPicture().getDisplay_url());
                    }
                }
            });

            txtTotalComents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion = getAdapterPosition();
                    if (postion != RecyclerView.NO_POSITION){
                        mOnClickComment.clickComment(homePosts.get(postion).getPicture().getShortcode());
                    }
                }
            });

        }
    }

    private OnClickComment mOnClickComment;
    public interface OnClickComment{
        void clickComment(String shortcode);
    }

    public void setOnClickComment(OnClickComment onClickComment){
        this.mOnClickComment = onClickComment;
    }

    private ClickRecyclerPostHome mClickHome;

    public interface ClickRecyclerPostHome{
        void moreOption(Picture picture);

        void image(String url);

        void profile( Person person);

        void totalLike(String shortcode);
    }



    public void setHomePosts(ClickRecyclerPostHome clickHome){
        this.mClickHome = clickHome;
    }
}
