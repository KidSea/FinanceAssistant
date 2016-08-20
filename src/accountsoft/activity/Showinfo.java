package accountsoft.activity;

import java.util.List;

import com.xiaoke.accountsoft.activity.R;

import accountsoft.dao.FlagDAO;
import accountsoft.dao.InaccountDAO;
import accountsoft.dao.OutaccountDAO;
import accountsoft.model.Tb_flag;
import accountsoft.model.Tb_inaccount;
import accountsoft.model.Tb_outaccount;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Showinfo extends ActionBarActivity {
	public static final String FLAG = "id";// ����һ��������������Ϊ������
	ListView lvinfo;// ����ListView����
	String strType = "";// �����ַ�������¼��������
	private List<Tb_outaccount> listoutinfos;
	private List<Tb_inaccount> listinfos;
	private List<Tb_flag> listFlags;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showinfo);// ���ò����ļ�
		lvinfo = (ListView) findViewById(R.id.lvinfo);// ��ȡ�����ļ��е�ListView���

		ShowInfo(R.id.it_showall);// Ĭ����ʾ֧����Ϣ

		lvinfo.setOnItemClickListener(new OnItemClickListener()// ΪListView�������¼�
		{
			// ��дonItemClick����
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (strType != "it_showall") {
					if (position != 0) {
						String strInfo = String.valueOf(((TextView) view)
								.getText());// ��¼����������Ϣ
						String strid = strInfo.substring(0,
								strInfo.indexOf(' '));// ������Ϣ�н�ȡ���
						Intent intent = null;// ����Intent����
						if (strType == "it_outcome" | strType == "it_income") {// �ж������֧������������Ϣ
							intent = new Intent(Showinfo.this, InfoManage.class);// ʹ��InfoManage���ڳ�ʼ��Intent����
							intent.putExtra(FLAG,
									new String[] { strid, strType });// ����Ҫ���ݵ�����
						} else if (strType == "it_flag") {// �ж�����Ǳ�ǩ��Ϣ
							intent = new Intent(Showinfo.this, FlagManage.class);// ʹ��FlagManage���ڳ�ʼ��Intent����
							intent.putExtra(FLAG, strid);// ����Ҫ���ݵ�����
						}
						startActivity(intent);// ִ��Intent������Ӧ��Activity
					} else {
						showError(1);
					}

				} else {
					showError(R.id.it_showall);
				}
			}
		});
		initActionBar();
	}

	protected void showError(int type) {
		// TODO Auto-generated method stub
		if (type  != R.id.it_showall){
			Toast.makeText(this, "��ѡ����ȷλ�õ��", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "ȫ������²�֧�ֵ���������", Toast.LENGTH_SHORT).show();
		}
		
	}

	private void initActionBar() {
		actionBar = getActionBar();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.it_showall:
			ShowInfo(R.id.it_showall);
			actionBar.setTitle("���ݹ���");
			//Toast.makeText(this, "��ʾȫ����Ϣ", Toast.LENGTH_SHORT).show();
			break;
		case R.id.it_outcome:
			// ���Ͻ�home�����֮�����Ӧ
			actionBar.setTitle("֧����Ϣ");
			ShowInfo(R.id.it_outcome);// ��ʾ֧����Ϣ
			//Toast.makeText(this, "֧����Ϣ", Toast.LENGTH_SHORT).show();
			break;
		case R.id.it_income:
			ShowInfo(R.id.it_income);// ��ʾ֧����Ϣ
			actionBar.setTitle("������Ϣ");
			//Toast.makeText(this, "������Ϣ", Toast.LENGTH_SHORT).show();
			break;
		case R.id.it_flag:
			ShowInfo(R.id.it_flag);// ��ʾ֧����Ϣ
			actionBar.setTitle("ҳǩ��Ϣ");
			//Toast.makeText(this, "ҳǩ��Ϣ", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void ShowInfo(int intType) {// �������ݴ���Ĺ������ͣ���ʾ��Ӧ����Ϣ
		String[] strInfos = null;// �����ַ������飬�����洢������Ϣ
		ArrayAdapter<String> arrayAdapter = null;// ����ArrayAdapter����
		double totaloutmoney = 0;
		double totalinmoney = 0;
		OutaccountDAO outaccountinfo = new OutaccountDAO(Showinfo.this);// ����OutaccountDAO����
		listoutinfos = outaccountinfo.getScrollData(0,
				(int) outaccountinfo.getCount());
		InaccountDAO inaccountinfo = new InaccountDAO(Showinfo.this);// ����InaccountDAO����
		listinfos = inaccountinfo.getScrollData(0,
				(int) inaccountinfo.getCount());
		FlagDAO flaginfo = new FlagDAO(Showinfo.this);// ����FlagDAO����
		listFlags = flaginfo.getScrollData(0, (int) flaginfo.getCount());
		switch (intType) {// ��intTypeΪ���������ж�
		case R.id.it_showall:
			strType = "it_showall";// ΪstrType������ֵ
			strInfos = new String[listoutinfos.size() + listinfos.size()
					+ listFlags.size() + 3];// �����ַ�������ĳ���
			int all = 1;// ����һ����ʼ��ʶ
			for (Tb_outaccount tb_outaccount : listoutinfos) {// ����List���ͼ���
				// ��֧�������Ϣ��ϳ�һ��D�ַ������洢���ַ����������Ӧλ��
				strInfos[all] = tb_outaccount.getid() + " |  "
						+ tb_outaccount.getType() + " "
						+ String.valueOf(tb_outaccount.getMoney()) + "Ԫ     "
						+ tb_outaccount.getTime();
				totaloutmoney += tb_outaccount.getMoney();
				all++;// ��ʶ��1
			}
			all++;
			strInfos[0] = "ȫ��֧��:  " + totaloutmoney + '\n' + "֧����Ϣ��: "
					+ (listoutinfos.size()) + "��";
			for (Tb_inaccount tb_inaccount : listinfos) {// ����List���ͼ���
				// �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
				strInfos[all] = tb_inaccount.getid() + " |  "
						+ tb_inaccount.getType() + " "
						+ String.valueOf(tb_inaccount.getMoney()) + "Ԫ     "
						+ tb_inaccount.getTime();
				totalinmoney += tb_inaccount.getMoney();
				all++;// ��ʶ��1
			}
			all++;
			strInfos[listoutinfos.size() + 1] = "ȫ������:  " + totalinmoney + '\n'
					+ "������Ϣ��: " + (listinfos.size()) + "��";
			for (Tb_flag tb_flag : listFlags) {// ����List���ͼ���
				// ����ǩ�����Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
				strInfos[all] = tb_flag.getid() + " |  " + tb_flag.getFlag();
				if (strInfos[all].length() > 15)// �жϱ�ǩ��Ϣ�ĳ����Ƿ����15
					strInfos[all] = strInfos[all].substring(0, 15) + "����";// ��λ�ô���15֮����ַ����á�������
				all++;// ��ʶ��1
			}
			strInfos[listoutinfos.size() + listinfos.size() + 2] = "����"
					+ (listFlags.size()) + "����ǩ��Ϣ";

			break;
		case R.id.it_outcome:// �����btnoutinfo��ť
			strType = "it_outcome";// ΪstrType������ֵ
			strInfos = new String[listoutinfos.size() + 1];// �����ַ�������ĳ���
			int i = 1;// ����һ����ʼ��ʶ
			for (Tb_outaccount tb_outaccount : listoutinfos) {// ����List���ͼ���
				// ��֧�������Ϣ��ϳ�һ��D�ַ������洢���ַ����������Ӧλ��
				strInfos[i] = tb_outaccount.getid() + " |  "
						+ tb_outaccount.getType() + " "
						+ String.valueOf(tb_outaccount.getMoney()) + "Ԫ     "
						+ tb_outaccount.getTime();
				totaloutmoney += tb_outaccount.getMoney();
				i++;// ��ʶ��1
			}
			strInfos[0] = "ȫ��֧��:  " + totaloutmoney + '\n' + "֧����Ϣ��: "
					+ (strInfos.length - 1) + "��";
			break;
		case R.id.it_income:// �����btnininfo��ť
			strType = "it_income";// ΪstrType������ֵ
			strInfos = new String[listinfos.size() + 1];// �����ַ�������ĳ���
			int m = 1;// ����һ����ʼ��ʶ
			for (Tb_inaccount tb_inaccount : listinfos) {// ����List���ͼ���
				// �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
				strInfos[m] = tb_inaccount.getid() + " |  "
						+ tb_inaccount.getType() + " "
						+ String.valueOf(tb_inaccount.getMoney()) + "Ԫ     "
						+ tb_inaccount.getTime();
				totalinmoney += tb_inaccount.getMoney();
				m++;// ��ʶ��1
			}
			strInfos[0] = "ȫ������:  " + totalinmoney + '\n' + "������Ϣ��: "
					+ (listinfos.size()) + "��";
			break;
		case R.id.it_flag:// �����btnflaginfo��ť
			strType = "it_flag";// ΪstrType������ֵ
			strInfos = new String[listFlags.size() + 1];// �����ַ�������ĳ���
			int n = 1;// ����һ����ʼ��ʶ
			for (Tb_flag tb_flag : listFlags) {// ����List���ͼ���
				// ����ǩ�����Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
				strInfos[n] = tb_flag.getid() + " |  " + tb_flag.getFlag();
				if (strInfos[n].length() > 15)// �жϱ�ǩ��Ϣ�ĳ����Ƿ����15
					strInfos[n] = strInfos[n].substring(0, 15) + "����";// ��λ�ô���15֮����ַ����á�������
				n++;// ��ʶ��1
			}
			strInfos[0] = "����" + (strInfos.length - 1) + "����ǩ��Ϣ";
			break;
		}
		// ʹ���ַ��������ʼ��ArrayAdapter����
		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, strInfos);
		lvinfo.setAdapter(arrayAdapter);// ΪListView�б���������Դ
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();// ʵ�ֻ����еķ���
		ShowInfo(R.id.it_showall);// ��ʾ֧����Ϣ
	}
}
