package com.example.scotlandyard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameList extends AppCompatActivity {
    static ListView gameListView;
    List<Game> games = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gameListView = findViewById(R.id.list_currentGames);

        //add dummy data to games for testing
        Game game1 = new Game("Samplename1", 4);
        game1.setCurrentMembers(2);
        Game game2 = new Game("Samplename2", 4);
        game2.setCurrentMembers(2);

        //add current games to list
        games.add(game1);
        games.add(game2);

        //setAdapter to listView to show all existing games
        gameListView.setAdapter(new MyListAdapter(this, R.layout.game_item, games));
    }


    //need adapter to design a list item and add it to list
    private class MyListAdapter extends ArrayAdapter<Game> {
        private int layout;

        public MyListAdapter(Context context, int resource, List<Game> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        //build the design
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = new ViewHolder();
            final Game game = getItem(position);

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);

                mainViewHolder.gameName = (TextView) convertView.findViewById(R.id.txtgameName);
                mainViewHolder.currentMembers = (TextView) convertView.findViewById(R.id.txtMemberCount);
                mainViewHolder.playGame = (Button) convertView.findViewById(R.id.btnPlayGame);
                mainViewHolder.playGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GameList.this, RegistrationActivty.class);
                        startActivity(intent);
                    }
                });
                convertView.setTag(mainViewHolder);

            }

            //set Text from stored variables to views
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.gameName.setText(game.getGameName());
            mainViewHolder.currentMembers.setText(Integer.toString(game.getCurrentMembers()) + "/" + Integer.toString(game.getMaxMembers()));

            //returns finished view
            return convertView;

        }
    }

    //what i need in my view
    public class ViewHolder {
        TextView gameName;
        TextView currentMembers;
        Button playGame;
    }
}