package com.pokemonbattlearena.android;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMultiplayer;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.gson.Gson;
import com.pokemonbattlearena.android.engine.database.Move;
import com.pokemonbattlearena.android.engine.database.Pokemon;
import com.pokemonbattlearena.android.engine.match.PokemonPlayer;
import com.pokemonbattlearena.android.engine.match.PokemonTeam;
import com.pokemonbattlearena.android.fragments.battle.BattleHomeFragment;
import com.pokemonbattlearena.android.fragments.chat.ChatHomeFragment;
import com.pokemonbattlearena.android.fragments.team.TeamsHomeFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.games.GamesStatusCodes.STATUS_OK;
import static com.google.android.gms.games.GamesStatusCodes.STATUS_REAL_TIME_MESSAGE_SEND_FAILED;
import static com.google.android.gms.games.GamesStatusCodes.STATUS_REAL_TIME_ROOM_NOT_JOINED;

public class BottomBarActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        RealTimeMessageReceivedListener,
        RoomUpdateListener,
        RoomStatusUpdateListener,
        RealTimeMultiplayer.ReliableMessageSentCallback,
        TeamsHomeFragment.OnPokemonTeamSelectedListener,
        BattleHomeFragment.OnBattleFragmentTouchListener {

    private static final int TEAM_SIZE_INT = 1;
    private static final int MIN_PLAYERS = 2;
    private PokemonBattleApplication mApplication = PokemonBattleApplication.getInstance();
    private final static String TAG = BottomBarActivity.class.getSimpleName();

    // GOOGLE PLAY GAMES FIELDS
    private static final int RC_SIGN_IN = 9001;
    private String mRoomCreatorId = null;
    private String mRoomId = null;
    private String mMyId = null;
    private ArrayList<Participant> mParticipants = null;

    // GOOGLE PLAY SIGN IN FIELDS
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    private FragmentManager mFragmentManager;
    private BattleHomeFragment mBattleHomeFragment;
    private TeamsHomeFragment mTeamsHomeFragment;
    private ChatHomeFragment mChatHomeFragment;

    private BottomBar mBottomBar;
    private SharedPreferences mPreferences;

    private PokemonPlayer mCurrentPokemonPlayer;

    //region Fragment callbacks
    public void onTeamSelected(String pokemonJSON) {
        Log.d(TAG, "Selected: " + pokemonJSON);
        if (mFragmentManager != null) {
            mBottomBar.selectTabWithId(R.id.tab_battle);
            setSavedTeam(pokemonJSON);
            displaySavedTeam();
            setCurrentPokemonPlayer();
        }
    }

    @Override
    public void onBattleNowClicked() {
        showProgressDialog();
        startMatchMaking();
    }

    @Override
    public void onMoveClicked(Move move) {

    }
    //endregion

    //region Activity hooks
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottombar);

        mPreferences = getPreferences(Context.MODE_PRIVATE);

        if (displaySavedTeam()) {
            setCurrentPokemonPlayer();
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        mBottomBar.setDefaultTab(R.id.tab_battle);

        mFragmentManager = getFragmentManager();

        // Button listeners

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        mApplication.setGoogleApiClient(googleApiClient);

        mTeamsHomeFragment = createTeamsHomeFragment();
        mBattleHomeFragment = new BattleHomeFragment();
        mChatHomeFragment = new ChatHomeFragment();

        mFragmentManager.beginTransaction()
                .add(R.id.container, mBattleHomeFragment, "battle")
                .commit();

        // Listens for a tab touch (Only first touch of new tab)
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_teams:
                        if (mTeamsHomeFragment != null && !mTeamsHomeFragment.isAdded()) {
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.container, mTeamsHomeFragment, "team")
                                    .commit();
                        }
                        if (mChatHomeFragment != null && mChatHomeFragment.isAdded()) {
                            mFragmentManager.beginTransaction().remove(mChatHomeFragment).commit();
                        }
                        if (mBattleHomeFragment != null && mBattleHomeFragment.isAdded()) {
                            mFragmentManager.beginTransaction().remove(mBattleHomeFragment).commit();
                        }
                        break;
                    case R.id.tab_battle:
                        if (mBattleHomeFragment != null && !mBattleHomeFragment.isAdded()) {
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.container, mBattleHomeFragment, "battle")
                                    .commit();
                        }
                        if (mTeamsHomeFragment != null && mTeamsHomeFragment.isAdded()) {
                            mFragmentManager.beginTransaction().remove(mTeamsHomeFragment).commit();
                        }
                        if (mChatHomeFragment != null && mChatHomeFragment.isAdded()) {
                            mFragmentManager.beginTransaction().remove(mChatHomeFragment).commit();
                        }
                        break;
                    case R.id.tab_chat:
                        if (mChatHomeFragment != null && !mChatHomeFragment.isAdded()) {
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.container, mChatHomeFragment, "chat")
                                    .commit();
                        }
                        if (mTeamsHomeFragment != null && mTeamsHomeFragment.isAdded()) {
                            mFragmentManager.beginTransaction().remove(mTeamsHomeFragment).commit();
                        }
                        if (mBattleHomeFragment != null && mBattleHomeFragment.isAdded()) {
                            mFragmentManager.beginTransaction().remove(mBattleHomeFragment).commit();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        // Listens for a tab touch (Only when 'reselected')
        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_teams:
                        Toast.makeText(BottomBarActivity.this, "Teams Again", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_battle:
                        Toast.makeText(BottomBarActivity.this, "Battle Again", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_chat:
                        Toast.makeText(BottomBarActivity.this, "Chat Again", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setCurrentPokemonPlayer() {
        mCurrentPokemonPlayer = new PokemonPlayer();
        mCurrentPokemonPlayer.setPokemonTeam(getSavedTeam());
    }

    @Override
    protected void onStart() {
        if (mApplication.getGoogleApiClient() != null && mApplication.getGoogleApiClient().isConnected()) {
            Log.w(TAG, "GameHelper: client was already connected on onStart()");
        } else {
            Log.d(TAG,"Connecting client.");
            mApplication.getGoogleApiClient().connect();
        }
        super.onStart();
    }
    //endregion

    //region Google API Client
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (bundle != null) {

            Invitation inv = bundle.getParcelable(Multiplayer.EXTRA_INVITATION);

            // check to see if we have an invite
            if (inv != null) {
                // accept invitation
                RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
                roomConfigBuilder.setInvitationIdToAccept(inv.getInvitationId());
                Games.RealTimeMultiplayer.join(mApplication.getGoogleApiClient(), roomConfigBuilder.build());

                // prevent screen from sleeping during handshake
                keepScreenOn();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mApplication.getGoogleApiClient().connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        if (mResolvingConnectionFailure) {
            // Already resolving
            return;
        }
        // If the sign in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mApplication.getGoogleApiClient(), connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }
    }
    //endregion

    //region Activity Result Callback
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        Log.e(TAG, "Result of activity");
        switch (requestCode) {
            case RC_SIGN_IN:
                mSignInClicked = false;
                mResolvingConnectionFailure = false;
                if (resultCode == RESULT_OK) {
                    mApplication.getGoogleApiClient().connect();
                } else {
                    // Bring up an error dialog to alert the user that sign-in
                    // failed. The R.string.signin_failure should reference an error
                    // string in your strings.xml file that tells the user they
                    // could not be signed in, such as "Unable to sign in."
                    BaseGameUtils.showActivityResultError(this,
                            requestCode, resultCode, R.string.signin_other_error);
                    Log.e(TAG, "Error signing in " + requestCode);
                }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
    //endregion

    //region RoomUpdateListener Callbacks
    @Override
    public void onRoomCreated(int statusCode, Room room) {
        Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
        if (statusCode != STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
            showGameError();
            return;
        }

        // save room ID so we can leave cleanly before the game starts.
        mRoomId = room.getRoomId();
        mRoomCreatorId = room.getCreatorId();
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
        if (statusCode != STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }
        updateRoom(room);
    }

    @Override
    public void onLeftRoom(int statusCode, String s) {
        // we have left the room; return to main screen.
        Log.d(TAG, "onLeftRoom, code " + statusCode);
    }

    @Override
    public void onRoomConnected(int statusCode, Room room) {
        Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
        if (statusCode != STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }
        updateRoom(room);
        if (shouldStartGame(room)) {
            Log.d(TAG, "We are going to start!");
            sendMessage();
        }
    }
    //endregion

    //region RoomStatusUpdateListener Callbacks
    // We treat most of the room update callbacks in the same way: we update our list of
    // participants and update the display. In a real game we would also have to check if that
    // change requires some action like removing the corresponding player avatar from the screen,
    // etc.
    @Override
    public void onPeerDeclined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onP2PDisconnected(String participant) {
    }

    @Override
    public void onP2PConnected(String participant) {
    }

    @Override
    public void onPeerJoined(Room room, List<String> arg1) {
        Log.d(TAG, "Peer joined room");
        updateRoom(room);
    }

    @Override
    public void onPeerLeft(Room room, List<String> peersWhoLeft) {
        updateRoom(room);
    }

    @Override
    public void onConnectedToRoom(Room room) {
        Log.d(TAG, "onConnectedToRoom.");

        //get participants and my ID:
        mParticipants = room.getParticipants();
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(mApplication.getGoogleApiClient()));

        // save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
        if(mRoomId==null)
            mRoomId = room.getRoomId();
        if(mRoomCreatorId==null)
            mRoomCreatorId = room.getCreatorId();
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        mRoomId = null;
        showGameError();
    }

    // Show error message about game being cancelled and return to main screen.
    void showGameError() {
        BaseGameUtils.makeSimpleDialog(this, getString(R.string.game_problem));
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        updateRoom(room);
    }

    @Override
    public void onRoomConnecting(Room room) {
        updateRoom(room);
    }

    @Override
    public void onPeersConnected(Room room, List<String> peers) {
        Log.d(TAG, "Peer(S) connected");
        updateRoom(room);
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> peers) {
        updateRoom(room);
    }
    //endregion

    //region RealTimeMessageListener Callbacks
    @Override
    public void onRealTimeMessageReceived(RealTimeMessage rtm) {
        byte[] buf = rtm.getMessageData();
        String bufferString = new String(buf);
        PokemonPlayer opponentPlayer = new Gson().fromJson(bufferString, PokemonPlayer.class);
        Log.d(TAG, "Message Received: " + bufferString);
        setupBattleUI(mCurrentPokemonPlayer, opponentPlayer);
        hideProgressDialog();
    }

    private void setupBattleUI(PokemonPlayer player, PokemonPlayer opponent) {
        if (mBattleHomeFragment != null && mBattleHomeFragment.isAdded()) {
            mBattleHomeFragment.setPlayer(player);
            mBattleHomeFragment.setOpponent(opponent);
            mBattleHomeFragment.setBattleVisible(true);
        }
    }

    @Override
    public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientId) {

        Log.d(TAG, "Realtime message sent to " + recipientId + " (onRealTimeMessageSent callback)");

        switch (statusCode) {
            case STATUS_OK:
                Log.d(TAG, "Message sent successfully");
                break;
            case STATUS_REAL_TIME_MESSAGE_SEND_FAILED:
                Log.d(TAG, "Message failed to send");
                break;
            case STATUS_REAL_TIME_ROOM_NOT_JOINED:
                Log.d(TAG, "Message failed to send because recipient is not in the room");
                break;
            default:
                break;
        }
    }
    //endregion

    //region Private Helper Methods
    private void sendMessage() {
        Log.d(TAG, "Sending Message");
        PokemonTeam team = getSavedTeam();
        PokemonPlayer currentPlayer = new PokemonPlayer();
        currentPlayer.setPokemonTeam(team);
        String player = new Gson().toJson(currentPlayer);
        byte[] message = player.getBytes();
        for (Participant p : mParticipants) {
            if (!p.getParticipantId().equals(mMyId)) {
                Games.RealTimeMultiplayer.sendReliableMessage(mApplication.getGoogleApiClient(), null, message,
                        mRoomId, p.getParticipantId());
                Log.d(TAG, "Reliable message sent (sendMessage()) + " + new String(message));
            }
        }
    }

    // create a RoomConfigBuilder that's appropriate for your implementation
    private RoomConfig.Builder makeBasicRoomConfigBuilder() {
        return RoomConfig.builder(this)
                .setRoomStatusUpdateListener(this)
                .setMessageReceivedListener(this);
    }

    private void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
            mRoomId = room.getRoomId();
            mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(mApplication.getGoogleApiClient()));
            mRoomCreatorId = room.getCreatorId();
        }
        if (mParticipants != null) {
            // update game states
        }
    }

    private boolean displaySavedTeam() {
        String teamJSON = mPreferences.getString("pokemonTeamJSON", "mew");
        if (!teamJSON.equals("mew")) {
            TextView savedText = (TextView) findViewById(R.id.saved_team_textview);
            ImageView savedImage = (ImageView) findViewById(R.id.saved_team_imageview);
            savedText.setVisibility(View.VISIBLE);
            savedImage.setVisibility(View.VISIBLE);
            PokemonTeam pokemonTeam = new Gson().fromJson(teamJSON, PokemonTeam.class);
            savedImage.setImageDrawable(getDrawableForPokemon(this, pokemonTeam.getPokemons().get(0).getName()));
            return true;
        }
        return false;
    }

    private void setSavedTeam(String pokemonJSON) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("pokemonTeamJSON", pokemonJSON).apply();
        editor.commit();
    }

    // Leave the room.
    private void leaveRoom() {
        Log.d(TAG, "Leaving room.");
        stopKeepingScreenOn();
        if (mRoomId != null) {
            Games.RealTimeMultiplayer.leave(mApplication.getGoogleApiClient(), this, mRoomId);
            mRoomId = null;
            mRoomCreatorId = null;
        }
    }

    private void startMatchMaking() {
        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).
        Bundle am = RoomConfig.createAutoMatchCriteria(1, 1, 0);

        // build the room config:
        RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
        roomConfigBuilder.setAutoMatchCriteria(am);
        RoomConfig roomConfig = roomConfigBuilder.build();

        // create room:
        Games.RealTimeMultiplayer.create(mApplication.getGoogleApiClient(), roomConfig);

        // prevent screen from sleeping during handshake
        keepScreenOn();
    }

    private PokemonTeam getSavedTeam() {
        String teamJSON = mPreferences.getString("pokemonTeamJSON", "mew");
        if (!teamJSON.equals("mew")) {
            return new Gson().fromJson(teamJSON, PokemonTeam.class);
        }
        return null;
    }

    private Drawable getDrawableForPokemon(Context c, String name) {
        String key = "ic_pokemon_" + name.toLowerCase();
        int id = c.getResources().getIdentifier(key, "drawable", c.getPackageName());
        return c.getDrawable(id);
    }


    private TeamsHomeFragment createTeamsHomeFragment() {
        TeamsHomeFragment teamsHomeFragment = new TeamsHomeFragment();
        // Set the team size
        Bundle teamArgs = new Bundle();
        teamArgs.putInt("teamSize", TEAM_SIZE_INT);
        teamsHomeFragment.setArguments(teamArgs);
        return teamsHomeFragment;
    }

    private boolean shouldStartGame(Room room) {
        int connectedPlayers = 0;
        for (Participant p : room.getParticipants()) {
            if (p.isConnectedToRoom()) ++connectedPlayers;
        }
        return connectedPlayers >= MIN_PLAYERS;
    }

    private void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Clears the flag that keeps the screen on.
    private void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    //endregion
}
