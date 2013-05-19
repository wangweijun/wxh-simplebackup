/**
 * 
 */
package com.cissoid.simplebackup.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cissoid.simplebackup.R;
import com.cissoid.simplebackup.Status;
import com.cissoid.simplebackup.util.BAEUtil;

/**
 * @author Wxh
 * 
 */
public class HomePageFragment extends Fragment
{
    public static final String ARG_SECTION_NUMBER = "section_number";
    public final int normalColor = Color.rgb(255, 255, 255);
    private Status status = null;
    private TextView statusSDCard;
    private TextView statusRoot;
    private TextView statusBusybox;
    private TextView statusBae;
    private TextView messageSDCard;
    private TextView messageRoot;
    private TextView messageBusybox;
    private TextView messageBae;
    private Button baeButton;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        // ((SimpleBackupApplication) (getActivity().getApplication()))
        // .getExecutorService().submit(new CheckStatusThread(this));
        new CheckStatusTask(this).execute();
    }

    public View onCreateView( LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState )
    {

        // setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.homepage, container, false);
        statusSDCard = (TextView) rootView
                .findViewById(R.id.home_status_sdcard);
        statusRoot = (TextView) rootView.findViewById(R.id.home_status_root);
        statusBusybox = (TextView) rootView
                .findViewById(R.id.home_status_busybox);
        statusBae = (TextView) rootView.findViewById(R.id.home_status_bae);
        messageSDCard = (TextView) rootView
                .findViewById(R.id.home_message_sdcard);
        messageRoot = (TextView) rootView.findViewById(R.id.home_message_root);
        messageBusybox = (TextView) rootView
                .findViewById(R.id.home_message_busybox);
        messageBae = (TextView) rootView.findViewById(R.id.home_message_bae);
        baeButton = (Button) rootView.findViewById(R.id.home_button_bae);
        if ( status != null )
            refresh(status);
        return rootView;
    }

    public void refresh( boolean bae )
    {
        status.setBae(bae);
        refresh(status);
    }

    public void refresh( Status status )
    {
        this.status = status;
        if ( status.isSdcard() )
        {
            statusSDCard.setText(R.string.home_status_success);
            // statusSDCard.setTextColor(Color.rgb(51, 181, 229));
            statusSDCard.setTextColor(normalColor);
            messageSDCard.setText(R.string.home_sdcard_success);
        }
        else
        {
            statusSDCard.setText(R.string.home_status_failed);
            // statusSDCard.setTextColor(Color.rgb(255, 68, 68));
            statusSDCard.setTextColor(Color.rgb(255, 255, 255));
            messageSDCard.setText(R.string.home_sdcard_false);
        }
        if ( status.isRoot() )
        {
            statusRoot.setText(R.string.home_status_success);
            // statusRoot.setTextColor(Color.rgb(51, 181, 229));
            statusRoot.setTextColor(normalColor);
            messageRoot.setText(R.string.home_root_success);
        }
        else
        {
            statusRoot.setText(R.string.home_status_failed);
            // statusRoot.setTextColor(Color.rgb(255, 68, 68));
            statusRoot.setTextColor(Color.rgb(255, 255, 255));
            messageRoot.setText(R.string.home_root_false);
        }
        if ( status.isBusybox() )
        {
            statusBusybox.setText(R.string.home_status_success);
            // statusBusybox.setTextColor(Color.rgb(51, 181, 229));
            statusBusybox.setTextColor(normalColor);
            messageBusybox.setText(R.string.home_busybox_success);
        }
        else
        {
            statusBusybox.setText(R.string.home_status_failed);
            // statusBusybox.setTextColor(Color.rgb(255, 68, 68));
            statusBusybox.setTextColor(Color.rgb(255, 255, 255));
            messageBusybox.setText(R.string.home_busybox_false);
        }
        if ( status.isBae() )
        {
            statusBae.setText(R.string.home_status_success);
            statusBae.setTextColor(normalColor);
            messageBae.setText(R.string.home_bae_success);
            baeButton.setText(R.string.home_button_logout);
            final HomePageFragment fragment = this;
            baeButton.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                    BAEUtil.logout(fragment);
                }
            });
        }
        else
        {
            statusBae.setText(R.string.home_status_failed);
            statusBae.setTextColor(Color.rgb(255, 255, 255));
            messageBae.setText(R.string.home_bae_failed);
            baeButton.setText(R.string.home_button_login);
            final HomePageFragment fragment = this;
            baeButton.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                    BAEUtil.login(fragment);
                }
            });
        }
    }
}
