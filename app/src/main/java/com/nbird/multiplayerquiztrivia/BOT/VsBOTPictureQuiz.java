package com.nbird.multiplayerquiztrivia.BOT;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.Dialog.DialogBotResult;
import com.nbird.multiplayerquiztrivia.Dialog.QuizCancelDialog;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.Dialog.WaitingVSInGameDialog;
import com.nbird.multiplayerquiztrivia.EXTRA.SongActivity;
import com.nbird.multiplayerquiztrivia.FIREBASE.HighestScore;
import com.nbird.multiplayerquiztrivia.FIREBASE.TotalScore;
import com.nbird.multiplayerquiztrivia.GENERATORS.ScoreGenerator;
import com.nbird.multiplayerquiztrivia.LL.LLManupulator;
import com.nbird.multiplayerquiztrivia.LL.LifeLine;
import com.nbird.multiplayerquiztrivia.Model.questionHolder;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class VsBOTPictureQuiz extends AppCompatActivity {
    TextView questionTextView,scoreBoard,timerText,oppoNameTextView,myNameTextView;
    Button option1,option2,option3,option4,nextButton;
    LinearLayout linearLayout,linearLayoutexpert,linearLayoutAudience,linearLayoutFiftyFifty,linearLayoutSwap;
    CardView audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL,clockCardView;
    LottieAnimationView anim11,anim12,anim13,anim14,anim15,anim16,anim17,anim18,anim19,anim20;
    LottieAnimationView anim21,anim22,anim23,anim24,anim25,anim26,anim27,anim28,anim29,anim30;
    ImageView myPic,picOppo,questionImage;
    Dialog loadingDialog;
    CountDownTimer countDownTimer;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    private List<questionHolder> list;
    ArrayList<LottieAnimationView> animationList;

    int fiftyfiftynum=0,audiencenum=0,swapnum=0,expertnum=0,lifelineSum=0,position=0,num=0,score=0,myPosition=-1,count,category,oppoLifelineSum=0,myScore;
    String myName,myPicURL;

    AppData appData;
    SongActivity songActivity;
    LLManupulator llManupulator;
    //    QuizTimer timer;
    LifeLine lifeLine;
    SupportAlertDialog supportAlertDialog;
    TotalScore totalScore;
    HighestScore highestScore;


    int oppoScoreCounter=0;
    int oppoWrongAnsCounter=0;

    int minutes=3,oppoMinute=3;
    int second=0,oppoSecond=0;
    String minutestext,oppominutestext;
    String secondtext,opposecondtext;

    CountDownTimer countDownTimerForBot,c;
    int botTime,botCorrectAns, timeTakenInt;
    ArrayList<LottieAnimationView> oppoAnimationViewList;



    ArrayList<Boolean> animList,oppoAnimList;

    int binaryPosition=0;
    WaitingVSInGameDialog waitingVSInGameDialog;

    boolean completedFirst=false;

    String oppoNameString,oppoImageURL;
    String timeTakenString;
    HashMap<String,Integer> map;

    String oppoTimeTakenString;


    NativeAd NATIVE_ADS;

    private InterstitialAd mInterstitialAd;
    private void loadAds(){


        String key=AppString.INTERSTITIAL_ID;

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, key, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });


    }
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vs_botpicture_quiz);





        category=getIntent().getIntExtra("category",1);

        oppoNameString=getIntent().getStringExtra("oppoName");
        oppoImageURL=getIntent().getStringExtra("oppoImageURL");

        Log.i("OPPO IMAGE",String.valueOf(oppoImageURL));


        list=new ArrayList<>();
        appData=new AppData();
        animationList=new ArrayList<>();
        animList=new ArrayList<>(12);
        oppoAnimList = new ArrayList<>(12);
        map=new HashMap<>();
        songStopperAndResumer();

        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, VsBOTPictureQuiz.this)){
            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);


            Random r=new Random();
            int num=r.nextInt(AppString.ADS_FREQUENCY_PICTURE);
            if(num==1){
                loadAds();
            }
        }

        questionTextView=findViewById(R.id.question);
        scoreBoard=findViewById(R.id.questionNumber);
        option1=(Button) findViewById(R.id.button1);
        option2=(Button) findViewById(R.id.button2);
        option3=(Button) findViewById(R.id.button3);
        option4=(Button) findViewById(R.id.button4);
        nextButton=(Button) findViewById(R.id.nextbutton);
        linearLayout=(LinearLayout) findViewById(R.id.linearButtonlayout);
        timerText=(TextView) findViewById(R.id.timer);
        myNameTextView=(TextView) findViewById(R.id.myNameTextView);
        oppoNameTextView=(TextView) findViewById(R.id.oppoNameTextView);
        audienceLL=(CardView) findViewById(R.id.audience);
        expertAdviceLL=(CardView) findViewById(R.id.expert);
        fiftyfiftyLL=(CardView) findViewById(R.id.fiftyfifty);
        swapTheQuestionLL=(CardView) findViewById(R.id.swap);
        linearLayoutexpert=(LinearLayout) findViewById(R.id.linearLayoutexpert) ;
        linearLayoutAudience=(LinearLayout) findViewById(R.id.linearLayoutAudience) ;
        linearLayoutFiftyFifty=(LinearLayout) findViewById(R.id.linearLayoutfiftyfifty) ;
        linearLayoutSwap=(LinearLayout) findViewById(R.id.linearLayoutSwap) ;
        anim11=(LottieAnimationView) findViewById(R.id.anim11);
        anim12=(LottieAnimationView) findViewById(R.id.anim12);
        anim13=(LottieAnimationView) findViewById(R.id.anim13);
        anim14=(LottieAnimationView) findViewById(R.id.anim14);
        anim15=(LottieAnimationView) findViewById(R.id.anim15);
        anim16=(LottieAnimationView) findViewById(R.id.anim16);
        anim17=(LottieAnimationView) findViewById(R.id.anim17);
        anim18=(LottieAnimationView) findViewById(R.id.anim18);
        anim19=(LottieAnimationView) findViewById(R.id.anim19);
        anim20=(LottieAnimationView) findViewById(R.id.anim20);
        anim21=(LottieAnimationView) findViewById(R.id.anim21);anim22=(LottieAnimationView) findViewById(R.id.anim22);
        anim23=(LottieAnimationView) findViewById(R.id.anim23);anim24=(LottieAnimationView) findViewById(R.id.anim24);anim25=(LottieAnimationView) findViewById(R.id.anim25);
        anim26=(LottieAnimationView) findViewById(R.id.anim26);anim27=(LottieAnimationView) findViewById(R.id.anim27);anim28=(LottieAnimationView) findViewById(R.id.anim28);
        anim29=(LottieAnimationView) findViewById(R.id.anim29);anim30=(LottieAnimationView) findViewById(R.id.anim30);
        myPic=(ImageView) findViewById(R.id.myPic);
        picOppo=(ImageView) findViewById(R.id.picOppo);
        questionImage=(ImageView) findViewById(R.id.questionImage);

        clockCardView = (CardView) findViewById(R.id.cardView3);

        myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, VsBOTPictureQuiz.this);
        myPicURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, VsBOTPictureQuiz.this);
        Glide.with(getBaseContext()).load(myPicURL).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(myPic);

        myNameTextView.setText(myName);
        oppoNameTextView.setText(oppoNameString);
        Glide.with(getBaseContext()).load(oppoImageURL).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(picOppo);

        llManupulator=new LLManupulator(audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL);

        animationList.add(anim11);animationList.add(anim12);animationList.add(anim13);animationList.add(anim14);animationList.add(anim15);
        animationList.add(anim16);animationList.add(anim17);animationList.add(anim18);animationList.add(anim19);animationList.add(anim20);

        supportAlertDialog=new SupportAlertDialog(loadingDialog,VsBOTPictureQuiz.this);
        supportAlertDialog.showLoadingDialog();

        lifeLine();
        questionSelector();

        c=new CountDownTimer(1000*15,1000) {
            @Override
            public void onTick(long l) {
                if(questionImage.getDrawable() != null){
                    try {
                        if(c!=null){
                            c.cancel();
                        }
                        setCountDownTimer(60000*3,1000,timerText,clockCardView);
                        botFunction();
                        supportAlertDialog.dismissLoadingDialog();
                    }catch (Exception e){

                    }

                }
            }

            @Override
            public void onFinish() {
                setCountDownTimer(60000*3,1000,timerText,clockCardView);
                botFunction();
                supportAlertDialog.dismissLoadingDialog();
            }
        }.start();

        totalScore=new TotalScore();
        totalScore.getSingleModeScore();

        highestScore=new HighestScore();
        highestScore.start();

        oppoAnimationViewList=new ArrayList<>();
        oppoAnimationViewList.add(anim21); oppoAnimationViewList.add(anim22); oppoAnimationViewList.add(anim23); oppoAnimationViewList.add(anim24); oppoAnimationViewList.add(anim25);
        oppoAnimationViewList.add(anim26); oppoAnimationViewList.add(anim27); oppoAnimationViewList.add(anim28); oppoAnimationViewList.add(anim29); oppoAnimationViewList.add(anim30);






    }

    public void lifeLine(){

        lifeLine=new LifeLine(linearLayoutFiftyFifty,linearLayoutAudience,linearLayoutexpert,position,list,option1,option2,option3,option4,myName,VsBOTPictureQuiz.this);

        fiftyfiftyLL.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { if(fiftyfiftynum==0) { lifelineSum++;fiftyfiftynum = 1;lifeLine.setPosition(position);lifeLine.fiftyfiftyLL(); }else{ lifeLine.LLUsed("FIFTY-FIFTY"); } }});
        audienceLL.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { if(audiencenum==0) { lifelineSum++;audiencenum = 1;lifeLine.setPosition(position);lifeLine.audienceLL(); }else{ lifeLine.LLUsed("AUDIENCE"); } }});


        swapTheQuestionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swapnum==0) {
                    lifelineSum++;swapnum=1;
                    linearLayoutSwap.setBackgroundResource(R.drawable.usedicon);
                    nextButton.setEnabled(false);nextButton.setAlpha(0.7f);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { enableOption(true); }
                    position++;llManupulator.True();count = 0;
                    playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                }else{ lifeLine.LLUsed("SWAP"); }
            }
        });

        expertAdviceLL.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { if(expertnum==0){ lifelineSum++;expertnum=1;lifeLine.setPosition(position);lifeLine.expertAdviceLL(); }else{ lifeLine.LLUsed("EXPERT ADVICE"); } }});
    }


    public void questionSelector() {
        for(int i=0;i<11;i++){
            Random rand = new Random();
            int setNumber = rand.nextInt(4999)+1;
            if(setNumber>1210&&setNumber<2000){
                setNumber=setNumber-1000;
            }
            fireBaseData2(setNumber);
        }
    }



    public void fireBaseData2 ( int setNumber){
        myRef.child("PictureQuizMain").child(String.valueOf(setNumber)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.add(snapshot.getValue(questionHolder.class));
                try{
                    Glide.with(getBaseContext())
                            .load(list.get(num).getQuestionPicture()).error((Drawable) Glide.with(getBaseContext()).load(list.get(num).getQuestionPicture()).error((Drawable) Glide.with(getBaseContext()).load(list.get(num).getQuestionPicture()).error((Drawable) Glide.with(getBaseContext()).load(list.get(num).getQuestionPicture()).preload(20,10)).preload(20,10)).preload(20,10))
                            .preload(20, 10);
                }catch (Exception e){

                }
                mainManupulations();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VsBOTPictureQuiz.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                supportAlertDialog.dismissLoadingDialog();
                finish();
            }
        });
    }


    public void setCountDownTimer(int totalTime, int interval, TextView clockTextView, CardView clockCardView){

        countDownTimer=new CountDownTimer(totalTime, interval) {


            @SuppressLint("ResourceAsColor")
            public void onTick(long millisUntilFinished) {


                if(second==0){
                    minutes--;
                    minutestext="0"+String.valueOf(minutes);
                    second=59;
                    if(second<10){
                        secondtext="0"+String.valueOf(second);
                    }else{
                        secondtext=String.valueOf(second);
                    }
                    clockTextView.setText(minutestext+":"+secondtext+" ");

                }else{
                    minutestext="0"+String.valueOf(minutes);
                    if(second<10){
                        secondtext="0"+String.valueOf(second);
                    }else{
                        secondtext=String.valueOf(second);
                    }
                    clockTextView.setText(minutestext+":"+secondtext+" ");
                    second--;
                }

                //Last 15 seconds end animation
                if(minutes==0 && second<=15){

                    clockTextView.setTextColor(Color.parseColor("#FF5E5E"));

                    //Continuous zoomIn - zoomOut
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(clockCardView, "scaleX", 0.9f, 1f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(clockCardView, "scaleY", 0.9f, 1f);

                    scaleX.setRepeatCount(ObjectAnimator.INFINITE);
                    scaleX.setRepeatMode(ObjectAnimator.REVERSE);

                    scaleY.setRepeatCount(ObjectAnimator.INFINITE);
                    scaleY.setRepeatMode(ObjectAnimator.REVERSE);

                    AnimatorSet scaleAnim = new AnimatorSet();
                    scaleAnim.setDuration(500);
                    scaleAnim.play(scaleX).with(scaleY);

                    scaleAnim.start();
                }

            }
            public void onFinish() {
                timerText.setText("00:00");
                minutes=0;
                second=0;
                Toast.makeText(VsBOTPictureQuiz.this, "Time Over", Toast.LENGTH_SHORT).show();
                quizFinishDialog();

            }

        }.start();

    }

    public void mainManupulations(){

        num++;
        if (num == 10) {

//                    timer=new QuizTimer(countDownTimer,60000*3,1000,VsBOTPictureQuiz.this,timerText,clockCardView);
//                    timer.start();
            if (list.size() > 0) {
                for (int i = 0; i < 4; i++) {
                    linearLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View view) {
                            try {
                                checkAnswer((Button) view);
                            } catch (Exception e) {
                                //        Toast.makeText(quizActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {

                        playMusic(R.raw.buttonmusic);
                        nextButton.setEnabled(false);
                        nextButton.setAlpha(0.7f);
                        enableOption(true);
                        position++;
                        llManupulator.True();

                        if (swapnum == 0) { if (position == 10) { quizFinishDialog();return; } } else { if (position == 11) { quizFinishDialog();return; } }
                        count = 0;
                        playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                    }
                });
            } else {
                finish();
                Toast.makeText(VsBOTPictureQuiz.this, "No Questions", Toast.LENGTH_SHORT).show();
            }
           // supportAlertDialog.dismissLoadingDialog();
        }
    }


    private void playAnim(final View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationStart(Animator animator) {
                if(value==0 && count<4){
                    String option="";
                    if(count==0){
                        String linkHolder=list.get(position).getQuestionPicture();
                        try{
                            Glide.with(getBaseContext())
                                    .load(linkHolder)
                                    .error(Glide.with(getBaseContext()).load(linkHolder).error(Glide.with(getBaseContext()).load(linkHolder).error(Glide.with(getBaseContext()).load(linkHolder))))
                                    .into(questionImage);
                        }catch (Exception e){

                        }



                        option=list.get(position).getOption1();
                        option1.setTextColor(Color.parseColor("#DEE7FF"));
                        linearLayout.getChildAt(0).setBackgroundResource(R.drawable.border_theme_2);

                    }else if(count==1){
                        option=list.get(position).getOption2();
                        option2.setTextColor(Color.parseColor("#DEE7FF"));
                        linearLayout.getChildAt(1).setBackgroundResource(R.drawable.border_theme_2);

                    }else if(count==2){
                        option=list.get(position).getOption3();
                        option3.setTextColor(Color.parseColor("#DEE7FF"));
                        linearLayout.getChildAt(2).setBackgroundResource(R.drawable.border_theme_2);

                    }else if(count==3){
                        option=list.get(position).getOption4();
                        option4.setTextColor(Color.parseColor("#DEE7FF"));
                        linearLayout.getChildAt(3).setBackgroundResource(R.drawable.border_theme_2);

                    }
                    playAnim(linearLayout.getChildAt(count),0,option);
                    count++;
                }
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                if (value == 0) {
                    try {
                        ((TextView) view).setText(data);
                        if(swapnum==0){
                            scoreBoard.setText((position+1)+"/10 ");
                        }else{
                            scoreBoard.setText((position)+"/10 ");
                        }
                    } catch (ClassCastException ex) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view, 1, data);
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) { }
            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
    }



    public void playMusic(int id){
        MediaPlayer musicNav;
        musicNav = MediaPlayer.create(VsBOTPictureQuiz.this,id);
        musicNav.start();
        musicNav.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                musicNav.reset();
                musicNav.release();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(Button selectedOption){
        enableOption(false);
        nextButton.setEnabled(true);
        nextButton.setAlpha(1);

        llManupulator.False();

        if(selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())){
            //correct
            playMusic(R.raw.correctmusic);
            ANIM_MANU(R.raw.tickanim);
            animList.add(true);
            selectedOption.setBackgroundResource(R.drawable.option_right);
            //green color
            selectedOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            selectedOption.setShadowLayer(3,1,1,R.color.green);
            score++;
        }else {
            //incorrect
            playMusic(R.raw.wrongansfinal);
            ANIM_MANU(R.raw.wronganim);
            animList.add(false);
            selectedOption.setBackgroundResource(R.drawable.option_wrong);     //red color
            selectedOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            selectedOption.setShadowLayer(3,1,1,R.color.green);
            Button correctOption = (Button) linearLayout.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOption.setBackgroundResource(R.drawable.option_right);    //green color
            correctOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            correctOption.setShadowLayer(3,1,1,R.color.red);
        }
    }

    public void ANIM_MANU(int id){
        myPosition++;
        LottieAnimationView anim=animationList.get(myPosition);
        anim.setAnimation(id);
        anim.playAnimation();
        anim.loop(false);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableOption(boolean enable){
        for (int i=0;i<4;i++) {
            linearLayout.getChildAt(i).setEnabled(enable);
            if (enable) {
                linearLayout.getChildAt(i).setBackgroundResource(R.drawable.option_null);
            }
        }
    }


    public void quizFinishDialog(){

        try{
            countDownTimer.cancel();
        }catch (Exception e){
            e.printStackTrace();
        }

        int minutesLeft=minutes;
        int secondsLeft=second;


        if((60-secondsLeft)>=10){
            if(secondsLeft==0){
                timeTakenString="0"+String.valueOf(2-minutesLeft+1)+":00";
            }else{
                timeTakenString="0"+String.valueOf(2-minutesLeft)+":"+String.valueOf(60-secondsLeft);
            }
        }else{
            timeTakenString="0"+String.valueOf(2-minutesLeft)+":0"+String.valueOf(60-secondsLeft);
        }

        timeTakenInt=((2-minutesLeft)*60)+(60-secondsLeft);

        ScoreGenerator scoreGenerator=new ScoreGenerator(minutes,second,lifelineSum,score);
        myScore=scoreGenerator.start();
        totalScore.setTotalScore(myScore+totalScore.getTotalScore());
        totalScore.setSingleModeScore();

        if(highestScore.getHighestScore()<myScore){
            highestScore.setHighestScore(myScore);
            highestScore.upLoadHighestScore(myScore);
        }




        map.put("Expert",expertnum);
        map.put("Flip",swapnum);
        map.put("Audience",audiencenum);
        map.put("Fifty-Fifty",fiftyfiftynum);







        if(binaryPosition<10){
            completedFirst=true;
            waitingVSInGameDialog =new WaitingVSInGameDialog(myPicURL,myName,String.valueOf(score), timeTakenString,String.valueOf(score*10),String.valueOf(lifelineSum), VsBOTPictureQuiz.this,questionTextView,NATIVE_ADS);
            waitingVSInGameDialog.start();
        }else{

            botData();
        }




    }




    private void botData(){
        Random random=new Random();
        oppoLifelineSum=random.nextInt(5);

        Log.i("oppoLifelineSum" , String.valueOf(oppoLifelineSum));


        HashMap<String,Integer> oppoMap=new HashMap<>();
        oppoMap.put("Expert",0);
        oppoMap.put("Audience",0);
        oppoMap.put("Fifty-Fifty",0);
        oppoMap.put("Flip",0);
        for(int i=0;i<oppoLifelineSum;){

            if(i<oppoLifelineSum){
                if(oppoMap.get("Expert")==0){
                    boolean isUse=random.nextBoolean();
                    if(isUse){
                        oppoMap.put("Expert",1);
                        i++;
                        Log.i("Expert","true");
                    }
                }
            }



            if(i<oppoLifelineSum){
                if(oppoMap.get("Audience")==0){
                    boolean isUse=random.nextBoolean();
                    if(isUse){
                        oppoMap.put("Audience",1);
                        i++;
                        Log.i("Audience","true");
                    }
                }
            }




            if(i<oppoLifelineSum){
                if(oppoMap.get("Fifty-Fifty")==0){
                    boolean isUse=random.nextBoolean();
                    if(isUse){
                        oppoMap.put("Fifty-Fifty",1);
                        i++;
                        Log.i("Fifty-Fifty","true");
                    }
                }
            }




            if(i<oppoLifelineSum){
                if(oppoMap.get("Flip")==0){
                    boolean isUse=random.nextBoolean();
                    if(isUse){
                        oppoMap.put("Flip",1);
                        i++;
                        Log.i("Flip","true");
                    }
                }
            }

        }





        Log.i("yo" , "1");
        ScoreGenerator oppoScoreGenerator=new ScoreGenerator(oppoMinute,oppoSecond,oppoLifelineSum,oppoScoreCounter);

        int oppoTotalScore=oppoScoreGenerator.start();
        int oppoTimeTakenInt=((2-oppoMinute)*60)+(60-oppoSecond);

        Log.i("oppoTimeTakenInt" , String.valueOf(oppoTimeTakenInt));


        if((60-oppoSecond)>=10){
            if(oppoSecond==0){
                oppoTimeTakenString="0"+String.valueOf(2-oppoMinute+1)+":00";
            }else{
                oppoTimeTakenString="0"+String.valueOf(2-oppoMinute)+":"+String.valueOf(60-oppoMinute);
            }
        }else{
            oppoTimeTakenString="0"+String.valueOf(2-oppoMinute)+":0"+String.valueOf(60-oppoMinute);
        }



        Log.i("oppoTimeTakenString" , String.valueOf(oppoTimeTakenString));


        for(int i=0;i<animList.size();i++){
            try{
                Log.i("ANIMATION LIST",String.valueOf(animList.get(i)));
            }catch (Exception e){

            }
        }


        if(mInterstitialAd!=null) {
            // Step 1: Display the interstitial
            mInterstitialAd.show(VsBOTPictureQuiz.this);
            // Step 2: Attach an AdListener
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);

                    DialogBotResult dialogBotResult=new DialogBotResult(myScore,score,category,timeTakenInt,lifelineSum,oppoTotalScore,oppoScoreCounter,oppoTimeTakenInt,oppoLifelineSum,audienceLL,VsBOTPictureQuiz.this,
                            myName,myPicURL,timeTakenString,oppoNameString,oppoImageURL,oppoTimeTakenString,map,oppoMap,animList,oppoAnimList,audienceLL,1);
                    dialogBotResult.start();

                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    DialogBotResult dialogBotResult=new DialogBotResult(myScore,score,category,timeTakenInt,lifelineSum,oppoTotalScore,oppoScoreCounter,oppoTimeTakenInt,oppoLifelineSum,audienceLL,VsBOTPictureQuiz.this,
                            myName,myPicURL,timeTakenString,oppoNameString,oppoImageURL,oppoTimeTakenString,map,oppoMap,animList,oppoAnimList,audienceLL,1);
                    dialogBotResult.start();
                }
            });


        }else{

            DialogBotResult dialogBotResult=new DialogBotResult(myScore,score,category,timeTakenInt,lifelineSum,oppoTotalScore,oppoScoreCounter,oppoTimeTakenInt,oppoLifelineSum,audienceLL,VsBOTPictureQuiz.this,
                    myName,myPicURL,timeTakenString,oppoNameString,oppoImageURL,oppoTimeTakenString,map,oppoMap,animList,oppoAnimList,audienceLL,1);
            dialogBotResult.start();

        }



    }


    public void songStopperAndResumer(){
        CardView cardViewSpeaker=(CardView) findViewById(R.id.cardViewSpeaker);
        final ImageView speakerImage=(ImageView) findViewById(R.id.speakerImage);
        final LinearLayout Speaker=(LinearLayout) findViewById(R.id.Speaker);
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,VsBOTPictureQuiz.this)){
            songActivity=new SongActivity(this);
            songActivity.startMusic();
        }else{
            Speaker.setBackgroundResource(R.drawable.usedicon);
            speakerImage.setBackgroundResource(R.drawable.music_off);
        }
        cardViewSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,VsBOTPictureQuiz.this)){
                    songActivity.songStop();
                    Speaker.setBackgroundResource(R.drawable.usedicon);
                    speakerImage.setBackgroundResource(R.drawable.music_off);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,VsBOTPictureQuiz.this,false);
                }else{
                    songActivity=new SongActivity(VsBOTPictureQuiz.this);
                    songActivity.startMusic();
                    Speaker.setBackgroundResource(R.drawable.single_color_2);
                    speakerImage.setBackgroundResource(R.drawable.music_on);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,VsBOTPictureQuiz.this,true);
                }
            }
        });
    }

    public void onBackPressed() {
        QuizCancelDialog quizCancelDialog=new QuizCancelDialog(VsBOTPictureQuiz.this,countDownTimer,option1,songActivity,NATIVE_ADS);
        quizCancelDialog.startForSinglePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{mAdView.destroy();}catch (Exception e){}
        try{mInterstitialAd=null;}catch (Exception e){}
        try{
            c.cancel();
        }catch (Exception e){

        }

        try{ songActivity.songStop(); }catch (Exception e){ e.printStackTrace(); }

        try{
            if(countDownTimer!=null){ countDownTimer.cancel();}
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            countDownTimerForBot.cancel();
        }catch (Exception e){

        }


        Runtime.getRuntime().gc();
    }



    public void botFunction(){
        countBot();
    }

    public void countBot(){
        Random r=new Random();
        final boolean[] marker = {false};
        final int[] jk = {r.nextInt(7) + 4};
        countDownTimerForBot=new CountDownTimer(1000*180,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(oppoSecond==0){
                    oppoMinute--;
                    oppoSecond=59;
                }else{
                    oppoSecond--;
                }

                if(marker[0]){
                    Boolean b=r.nextBoolean();
                    int ans;
                    if(b){
                        botCorrectAns++;
                        ans=1;
                    }else{
                        ans=2;
                    }
                    binaryPosition++;
                    animManupulation(ans,binaryPosition);
                    marker[0] =false;

                    jk[0] =r.nextInt(7)+4;

                    if(binaryPosition<10){

                    }else{
                        if(completedFirst){

                            try{waitingVSInGameDialog.dismiss();}catch (Exception e){e.printStackTrace();}
                            botData();
                            try{
                                countDownTimerForBot.cancel();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            try{
                                countDownTimerForBot.cancel();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }


                }else{
                    jk[0]--;
                    if(jk[0]==0){
                        marker[0] =true;
                    }



                }


            }

            @Override
            public void onFinish() {

                try{
                    countDownTimerForBot.cancel();
                }catch (Exception e){
                    e.printStackTrace();
                }




            }
        }.start();
    }

    public void animManupulation(int opponentAnswer, int binaryPosition){

        try{
            LottieAnimationView lottieAnimationView=oppoAnimationViewList.get(binaryPosition-1);

            if(opponentAnswer ==1){
                oppoAnimList.add(true);
                oppoScoreCounter++;
                lottieAnimationView.setAnimation(R.raw.tickanim);
                lottieAnimationView.playAnimation();
                lottieAnimationView.loop(false);
            }else{
                oppoAnimList.add(false);
                oppoWrongAnsCounter++;
                lottieAnimationView.setAnimation(R.raw.wronganim);
                lottieAnimationView.playAnimation();
                lottieAnimationView.loop(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }



}