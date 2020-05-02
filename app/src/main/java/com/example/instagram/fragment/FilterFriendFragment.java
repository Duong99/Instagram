package com.example.instagram.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.Common;
import com.example.instagram.R;
import com.example.instagram.dialog.SelectPerson;
import com.example.instagram.Utils;
import com.example.instagram.adapter.PeopleFilterAdapter;
import com.example.instagram.main.LoginSuccessActivity;
import com.example.instagram.LoadingWebAPI;
import com.example.instagram.model.Person;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

public class FilterFriendFragment extends Fragment implements PeopleFilterAdapter.ClickPeopleFilter,
       LoadingWebAPI.GetPeopleFollow, PeopleFilterAdapter.GetSizeListFilter {

    private View view;
    private LoginSuccessActivity loginSuccessActivity;
    private ArrayList<Person> peopleFollowing, peopleFollowers, peopleNot;
    private AsyncHttpClient client = new AsyncHttpClient();
    private RecyclerView recyclerPeopleFilter;
    private PeopleFilterAdapter filterAdapter;
    private TextView txtTotalFriend;
    private Spinner spinnerContentWatch;
    private List<String> listContentWatch;
    private ArrayAdapter adapterListContentWatch;
    private LoadingWebAPI api;
    private String idProfile = null;
    private SearchView searchView;

    public FilterFriendFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter_friend, container, false);
        idProfile = loginSuccessActivity.getId();

        init();

        onCliclEvents();
        return view;
    }

    private void onCliclEvents() {
        spinnerContentWatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        txtTotalFriend.setText("Please! Select Item");
                        recyclerPeopleFilter.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        recyclerPeopleFilter.setVisibility(View.VISIBLE);
                        if (peopleFollowing.size() == 0) {
                            peopleFollowing = new ArrayList<>();
                            api = new LoadingWebAPI(peopleFollowing);
                            api.setPeopleFollow(FilterFriendFragment.this);
                            String url = Common.URL_Following1 + idProfile + Common.URL_Follow2 + Common.URL_Follow5;
                            api.getFollowingDataJson(url, idProfile + "");
                        } else {
                            setAdapter(peopleFollowing);
                        }
                        break;
                    case 2:
                        recyclerPeopleFilter.setVisibility(View.VISIBLE);
                        if (peopleFollowers.size() == 0) {
                            peopleFollowers = new ArrayList<>();
                            api = new LoadingWebAPI(peopleFollowers);
                            api.setPeopleFollow(FilterFriendFragment.this);
                            String url = Common.URL_Followers1 + idProfile + Common.URL_Follow2 + Common.URL_Follow5;
                            api.getFollowersDataJson(url, idProfile);
                        } else {
                            setAdapter(peopleFollowers);
                        }

                        break;
                    case 3:
                        if (peopleFollowing.size() == 0 || peopleFollowers.size() == 0) {
                            checkSizeList();
                        } else {
                            recyclerPeopleFilter.setVisibility(View.VISIBLE);
                            peopleNot = new ArrayList<>();
                            loadingPeopleNotFollowingBack();
                        }
                        break;
                    case 4:
                        if (peopleFollowing.size() == 0 || peopleFollowers.size() == 0) {
                            checkSizeList();
                        } else {
                            recyclerPeopleFilter.setVisibility(View.VISIBLE);
                            peopleNot = new ArrayList<>();
                            loadingPeopleNotFollowersBack();
                        }
                        break;
                    case 5:
                        if (peopleFollowing.size() == 0 || peopleFollowers.size() == 0) {
                            checkSizeList();
                        } else {
                            recyclerPeopleFilter.setVisibility(View.VISIBLE);
                            peopleNot = new ArrayList<>();
                            allFriend();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!(spinnerContentWatch.getSelectedItem().equals("None"))){
                    filterAdapter.getFilter().filter(searchView.getQuery());
                }
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                switch (spinnerContentWatch.getSelectedItemPosition()) {
                    case 0:
                        txtTotalFriend.setText("Please! Select Item");
                        recyclerPeopleFilter.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        setAdapter(peopleFollowing);
                        break;
                    case 2:
                        setAdapter(peopleFollowers);
                        break;
                    case 3:
                        setAdapter(peopleNot);
                        break;
                    case 4:
                        setAdapter(peopleNot);
                        break;
                    case 5:
                        setAdapter(peopleNot);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void allFriend() {
        for (int i=0; i<peopleFollowing.size(); i++){
            for (int j = 0; j < peopleFollowers.size(); j++){
                if (peopleFollowing.get(i).getIdPerson().equals(peopleFollowers.get(j).getIdPerson())){
                    peopleNot.add(new Person(peopleFollowing.get(i).getIdPerson(),
                            peopleFollowing.get(i).getUsername(),
                            peopleFollowing.get(i).getFullName(),
                            peopleFollowing.get(i).getImvPeron()));
                    break;
                }
            }
        }

        setAdapter(peopleNot);
    }

    private void loadingPeopleNotFollowersBack() {
        boolean check;
        for (int i = 0; i < peopleFollowers.size(); i++) {
            check = false;
            for (int j = 0; j < peopleFollowing.size(); j++) {
                if (peopleFollowers.get(i).getIdPerson().equals(peopleFollowing.get(j).getIdPerson())) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                peopleNot.add(new Person(peopleFollowers.get(i).getIdPerson(),
                        peopleFollowers.get(i).getUsername(),
                        peopleFollowers.get(i).getFullName(),
                        peopleFollowers.get(i).getImvPeron()));
            }
        }
        setAdapter(peopleNot);
    }

    private void loadingPeopleNotFollowingBack() {
        boolean check;
        for (int i = 0; i < peopleFollowing.size(); i++) {
            check = false;
            for (int j = 0; j < peopleFollowers.size(); j++) {
                if (peopleFollowing.get(i).getIdPerson().equals(peopleFollowers.get(j).getIdPerson())) {
                    check = true;
                    break;
                }
            }

            if (!check) {
                peopleNot.add(new Person(peopleFollowing.get(i).getIdPerson(),
                        peopleFollowing.get(i).getUsername(),
                        peopleFollowing.get(i).getFullName(),
                        peopleFollowing.get(i).getImvPeron()));
            }
        }
        setAdapter(peopleNot);
    }

    private void init() {
        searchView = view.findViewById(R.id.edtSearchFriend);
        spinnerContentWatch = view.findViewById(R.id.spinnerContentWatch);
        txtTotalFriend = view.findViewById(R.id.txtTotalFriend);

        client.addHeader("cookie", Utils.getCookieInstagram());
        peopleFollowing = new ArrayList<>();
        peopleFollowers = new ArrayList<>();
        listContentWatch = new ArrayList<>();
        peopleFollowing = new ArrayList<>();

        filterAdapter = new PeopleFilterAdapter(getContext(), peopleFollowing);

        recyclerPeopleFilter = view.findViewById(R.id.recyclerPeopleFilter);
        recyclerPeopleFilter.setHasFixedSize(true);
        recyclerPeopleFilter.setLayoutManager(new LinearLayoutManager(getContext()));

        addListContetWatch();
    }

    private void addListContetWatch() {
        listContentWatch.add("None");
        listContentWatch.add("Following");
        listContentWatch.add("Followers");
        listContentWatch.add("Not Following Back");
        listContentWatch.add("Not Followers Back");
        listContentWatch.add("All Friend");

        adapterListContentWatch = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, listContentWatch);
        spinnerContentWatch.setAdapter(adapterListContentWatch);
    }

    private void checkSizeList(){
        if (peopleFollowing.size() == 0 && peopleFollowers.size() == 0) {
            txtTotalFriend.setText("Please! Preselect Item Following and Followers");
            Toast.makeText(getContext(), "Please! Preselect Item Following and Followers", Toast.LENGTH_SHORT).show();
        } else if (peopleFollowing.size() == 0) {
            txtTotalFriend.setText("Please! Preselect Item Following");
            Toast.makeText(getContext(), "Please! Preselect Item Following", Toast.LENGTH_SHORT).show();
        } else {
            txtTotalFriend.setText("Please! Preselect Item Followers");
            Toast.makeText(getContext(), "Please! Preselect Item Followers", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginSuccessActivity) {
            loginSuccessActivity = (LoginSuccessActivity) context;
        }
    }


    private void setAdapter(ArrayList<Person> people) {
        filterAdapter = new PeopleFilterAdapter(getContext(), people);
        filterAdapter.setSizeListFilter(FilterFriendFragment.this);
        filterAdapter.setClickPeopleFilter(FilterFriendFragment.this);

        recyclerPeopleFilter.setAdapter(filterAdapter);

        txtTotalFriend.setText(String.format("%s Friends", people.size()));
    }

    @Override
    public void onClickPeople(final int position, final ArrayList<Person> people) {
        new SelectPerson(getContext(), people.get(position));
    }

    @Override
    public void getPeopleFollowing(ArrayList<Person> peopleFollowing) {
        this.peopleFollowing = peopleFollowing;
        setAdapter(peopleFollowing);
    }

    @Override
    public void getPeopleFollowers(ArrayList<Person> peopleFollowers) {
        this.peopleFollowers = peopleFollowers;
        setAdapter(peopleFollowers);
    }

    @Override
    public void sizeListFilter(int size) {
        txtTotalFriend.setText(String.format("%s Friends", size));
    }
}
