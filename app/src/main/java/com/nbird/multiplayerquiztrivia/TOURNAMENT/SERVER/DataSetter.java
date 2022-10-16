package com.nbird.multiplayerquiztrivia.TOURNAMENT.SERVER;

import android.widget.Adapter;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.PlayerDataAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;

import java.util.ArrayList;

public class DataSetter {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    public DataSetter() {
    }

    public void getPlayerData(String roomCode, ArrayList<Details> playerDataArrayList, PlayerDataAdapter myAdapter, ValueEventListener valueEventListener){


        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                playerDataArrayList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    LeaderBoardHolder leaderBoardHolder=dataSnapshot.getValue(LeaderBoardHolder.class);
                    float acc=((leaderBoardHolder.getCorrect()*100)/leaderBoardHolder.getWrong());

                    int min=leaderBoardHolder.getTotalTime()/60;
                    int sec=leaderBoardHolder.getTotalTime()%60;

                    String totalTime=min+" min "+sec+" sec";
                    String accStr=acc+"%";
                    String highestScore=String.valueOf(leaderBoardHolder.getScore());
                    playerDataArrayList.add(new Details(leaderBoardHolder.getImageUrl(),leaderBoardHolder.getUsername(),totalTime,accStr,highestScore,dataSnapshot.getKey()));

                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).addValueEventListener(valueEventListener);



    }

}