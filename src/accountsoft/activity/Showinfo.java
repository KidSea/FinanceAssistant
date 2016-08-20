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
	public static final String FLAG = "id";// 定义一个常量，用来作为请求码
	ListView lvinfo;// 创建ListView对象
	String strType = "";// 创建字符串，记录管理类型
	private List<Tb_outaccount> listoutinfos;
	private List<Tb_inaccount> listinfos;
	private List<Tb_flag> listFlags;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showinfo);// 设置布局文件
		lvinfo = (ListView) findViewById(R.id.lvinfo);// 获取布局文件中的ListView组件

		ShowInfo(R.id.it_showall);// 默认显示支出信息

		lvinfo.setOnItemClickListener(new OnItemClickListener()// 为ListView添加项单击事件
		{
			// 覆写onItemClick方法
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (strType != "it_showall") {
					if (position != 0) {
						String strInfo = String.valueOf(((TextView) view)
								.getText());// 记录单击的项信息
						String strid = strInfo.substring(0,
								strInfo.indexOf(' '));// 从项信息中截取编号
						Intent intent = null;// 创建Intent对象
						if (strType == "it_outcome" | strType == "it_income") {// 判断如果是支出或者收入信息
							intent = new Intent(Showinfo.this, InfoManage.class);// 使用InfoManage窗口初始化Intent对象
							intent.putExtra(FLAG,
									new String[] { strid, strType });// 设置要传递的数据
						} else if (strType == "it_flag") {// 判断如果是便签信息
							intent = new Intent(Showinfo.this, FlagManage.class);// 使用FlagManage窗口初始化Intent对象
							intent.putExtra(FLAG, strid);// 设置要传递的数据
						}
						startActivity(intent);// 执行Intent，打开相应的Activity
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
			Toast.makeText(this, "请选择正确位置点击", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "全显情况下不支持点击，请调整", Toast.LENGTH_SHORT).show();
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
			actionBar.setTitle("数据管理");
			//Toast.makeText(this, "显示全部信息", Toast.LENGTH_SHORT).show();
			break;
		case R.id.it_outcome:
			// 左上角home处点击之后的响应
			actionBar.setTitle("支出信息");
			ShowInfo(R.id.it_outcome);// 显示支出信息
			//Toast.makeText(this, "支出信息", Toast.LENGTH_SHORT).show();
			break;
		case R.id.it_income:
			ShowInfo(R.id.it_income);// 显示支出信息
			actionBar.setTitle("收入信息");
			//Toast.makeText(this, "收入信息", Toast.LENGTH_SHORT).show();
			break;
		case R.id.it_flag:
			ShowInfo(R.id.it_flag);// 显示支出信息
			actionBar.setTitle("页签信息");
			//Toast.makeText(this, "页签信息", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void ShowInfo(int intType) {// 用来根据传入的管理类型，显示相应的信息
		String[] strInfos = null;// 定义字符串数组，用来存储收入信息
		ArrayAdapter<String> arrayAdapter = null;// 创建ArrayAdapter对象
		double totaloutmoney = 0;
		double totalinmoney = 0;
		OutaccountDAO outaccountinfo = new OutaccountDAO(Showinfo.this);// 创建OutaccountDAO对象
		listoutinfos = outaccountinfo.getScrollData(0,
				(int) outaccountinfo.getCount());
		InaccountDAO inaccountinfo = new InaccountDAO(Showinfo.this);// 创建InaccountDAO对象
		listinfos = inaccountinfo.getScrollData(0,
				(int) inaccountinfo.getCount());
		FlagDAO flaginfo = new FlagDAO(Showinfo.this);// 创建FlagDAO对象
		listFlags = flaginfo.getScrollData(0, (int) flaginfo.getCount());
		switch (intType) {// 以intType为条件进行判断
		case R.id.it_showall:
			strType = "it_showall";// 为strType变量赋值
			strInfos = new String[listoutinfos.size() + listinfos.size()
					+ listFlags.size() + 3];// 设置字符串数组的长度
			int all = 1;// 定义一个开始标识
			for (Tb_outaccount tb_outaccount : listoutinfos) {// 遍历List泛型集合
				// 将支出相关信息组合成一个D字符串，存储到字符串数组的相应位置
				strInfos[all] = tb_outaccount.getid() + " |  "
						+ tb_outaccount.getType() + " "
						+ String.valueOf(tb_outaccount.getMoney()) + "元     "
						+ tb_outaccount.getTime();
				totaloutmoney += tb_outaccount.getMoney();
				all++;// 标识加1
			}
			all++;
			strInfos[0] = "全部支出:  " + totaloutmoney + '\n' + "支出信息共: "
					+ (listoutinfos.size()) + "条";
			for (Tb_inaccount tb_inaccount : listinfos) {// 遍历List泛型集合
				// 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
				strInfos[all] = tb_inaccount.getid() + " |  "
						+ tb_inaccount.getType() + " "
						+ String.valueOf(tb_inaccount.getMoney()) + "元     "
						+ tb_inaccount.getTime();
				totalinmoney += tb_inaccount.getMoney();
				all++;// 标识加1
			}
			all++;
			strInfos[listoutinfos.size() + 1] = "全部收入:  " + totalinmoney + '\n'
					+ "收入信息共: " + (listinfos.size()) + "条";
			for (Tb_flag tb_flag : listFlags) {// 遍历List泛型集合
				// 将便签相关信息组合成一个字符串，存储到字符串数组的相应位置
				strInfos[all] = tb_flag.getid() + " |  " + tb_flag.getFlag();
				if (strInfos[all].length() > 15)// 判断便签信息的长度是否大于15
					strInfos[all] = strInfos[all].substring(0, 15) + "……";// 将位置大于15之后的字符串用……代替
				all++;// 标识加1
			}
			strInfos[listoutinfos.size() + listinfos.size() + 2] = "共有"
					+ (listFlags.size()) + "条便签信息";

			break;
		case R.id.it_outcome:// 如果是btnoutinfo按钮
			strType = "it_outcome";// 为strType变量赋值
			strInfos = new String[listoutinfos.size() + 1];// 设置字符串数组的长度
			int i = 1;// 定义一个开始标识
			for (Tb_outaccount tb_outaccount : listoutinfos) {// 遍历List泛型集合
				// 将支出相关信息组合成一个D字符串，存储到字符串数组的相应位置
				strInfos[i] = tb_outaccount.getid() + " |  "
						+ tb_outaccount.getType() + " "
						+ String.valueOf(tb_outaccount.getMoney()) + "元     "
						+ tb_outaccount.getTime();
				totaloutmoney += tb_outaccount.getMoney();
				i++;// 标识加1
			}
			strInfos[0] = "全部支出:  " + totaloutmoney + '\n' + "支出信息共: "
					+ (strInfos.length - 1) + "条";
			break;
		case R.id.it_income:// 如果是btnininfo按钮
			strType = "it_income";// 为strType变量赋值
			strInfos = new String[listinfos.size() + 1];// 设置字符串数组的长度
			int m = 1;// 定义一个开始标识
			for (Tb_inaccount tb_inaccount : listinfos) {// 遍历List泛型集合
				// 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
				strInfos[m] = tb_inaccount.getid() + " |  "
						+ tb_inaccount.getType() + " "
						+ String.valueOf(tb_inaccount.getMoney()) + "元     "
						+ tb_inaccount.getTime();
				totalinmoney += tb_inaccount.getMoney();
				m++;// 标识加1
			}
			strInfos[0] = "全部收入:  " + totalinmoney + '\n' + "收入信息共: "
					+ (listinfos.size()) + "条";
			break;
		case R.id.it_flag:// 如果是btnflaginfo按钮
			strType = "it_flag";// 为strType变量赋值
			strInfos = new String[listFlags.size() + 1];// 设置字符串数组的长度
			int n = 1;// 定义一个开始标识
			for (Tb_flag tb_flag : listFlags) {// 遍历List泛型集合
				// 将便签相关信息组合成一个字符串，存储到字符串数组的相应位置
				strInfos[n] = tb_flag.getid() + " |  " + tb_flag.getFlag();
				if (strInfos[n].length() > 15)// 判断便签信息的长度是否大于15
					strInfos[n] = strInfos[n].substring(0, 15) + "……";// 将位置大于15之后的字符串用……代替
				n++;// 标识加1
			}
			strInfos[0] = "共有" + (strInfos.length - 1) + "条便签信息";
			break;
		}
		// 使用字符串数组初始化ArrayAdapter对象
		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, strInfos);
		lvinfo.setAdapter(arrayAdapter);// 为ListView列表设置数据源
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();// 实现基类中的方法
		ShowInfo(R.id.it_showall);// 显示支出信息
	}
}
