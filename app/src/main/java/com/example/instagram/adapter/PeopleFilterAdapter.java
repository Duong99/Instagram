package com.example.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.model.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleFilterAdapter extends RecyclerView.Adapter<PeopleFilterAdapter.Viewholder> implements Filterable {
    private Context context;
    private ArrayList<Person> people;
    private ArrayList<Person> peopleFilter;
    private CustomFilter filter;

    public PeopleFilterAdapter(Context context, ArrayList<Person> people) {
        this.context = context;
        this.people = people;
        this.peopleFilter = people;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_people_recycler_filter, null);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.txtFullnameFilter.setText(people.get(position).getFullName());
        holder.txtusernameFilter.setText(people.get(position).getUsername());

        Picasso.with(context).load(people.get(position).getImvPeron())
                .error(R.drawable.ic_launcher_background).into(holder.imvPeopleFilter);
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new CustomFilter();
        }
        return filter;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        CircleImageView imvPeopleFilter;
        TextView txtFullnameFilter, txtusernameFilter;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imvPeopleFilter = itemView.findViewById(R.id.imvPeopeoFilter);
            txtFullnameFilter = itemView.findViewById(R.id.txtFullNameFilter);
            txtusernameFilter = itemView.findViewById(R.id.txtUserNameFilter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        clickPeopleFilter.onClickPeople(position, people);
                    }
                }
            });
        }
    }
    private ClickPeopleFilter clickPeopleFilter;

    public interface ClickPeopleFilter{
        void onClickPeople(int position, ArrayList<Person> people);
    }

    public void setClickPeopleFilter(ClickPeopleFilter clickPeopleFilter){
        this.clickPeopleFilter = clickPeopleFilter;
    }

    private class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Person> filters = new ArrayList<>();
            if (constraint != null && constraint.length() > 0){
                // CONSTRAINT TO UPPER
                constraint = constraint.toString().toUpperCase();
                boolean check;
                for (int i=0; i<peopleFilter.size(); i++){
                    check = false;
                    if (peopleFilter.get(i).getUsername().toUpperCase().contains(constraint)) {
                        check = true;
                        Person p = new Person(peopleFilter.get(i).getIdPerson(),
                                peopleFilter.get(i).getUsername(),
                                peopleFilter.get(i).getFullName(),
                                peopleFilter.get(i).getImvPeron());
                        filters.add(p);
                    }else if (peopleFilter.get(i).getFullName().toUpperCase().contains(constraint)){
                        if (!check){
                            Person p = new Person(peopleFilter.get(i).getIdPerson(),
                                    peopleFilter.get(i).getUsername(),
                                    peopleFilter.get(i).getFullName(),
                                    peopleFilter.get(i).getImvPeron());
                            filters.add(p);
                        }
                    }
                }
            }
            results.count = filters.size();
            results.values = filters;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            people = (ArrayList<Person>) results.values;
            getSizeListFilter.sizeListFilter(people.size());
            notifyDataSetChanged();
        }
    }
    private GetSizeListFilter getSizeListFilter;

    public interface GetSizeListFilter{
        void sizeListFilter(int size);
    }

    public void setSizeListFilter(GetSizeListFilter getSizeListFilter){
        this.getSizeListFilter = getSizeListFilter;
    }
}
