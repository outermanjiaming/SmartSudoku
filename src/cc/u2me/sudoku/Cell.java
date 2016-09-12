package cc.u2me.sudoku;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Cell {
	int loc;
	int r;
    int c;
    int blockLoc;
    Administrator a;
	Set<Integer> candidates = new HashSet<Integer>();
	Set<Integer> initial = new HashSet<Integer>();
	Set<Integer> conflicts = null;
	Stack<Integer> tests = new Stack<Integer>();
	Cell(int loc, Administrator admin){
		this.a = admin;
		this.loc = loc;
		this.r   = loc/9;
		this.c   = loc%9;
		this.blockLoc = (r/3)*3+c/3;
		candidates.add(1);
		candidates.add(2);
		candidates.add(3);
		candidates.add(4);
		candidates.add(5);
		candidates.add(6);
		candidates.add(7);
		candidates.add(8);
		candidates.add(9);
	}
	
	void candidate(int[] cells){
		//find the positions which are conflict
		if(conflicts == null) {
			conflicts = new HashSet<Integer>();
			for(int i = 0; i < 9; i ++) {
				int row = r*9 + i;
				int col = i*9 + c;
				int bloc = (blockLoc/3 *3 + i/3) * 9 + (blockLoc%3 * 3 + i%3);
				//System.out.println("row["+i+","+r+"]:"+row + " col["+i+","+c+"]:"+col + " bloc["+i+","+blockLoc+"]:"+bloc);
				conflicts.add(row);
				conflicts.add(col);
				conflicts.add(bloc);
			}
			remove(cells);
			initial.addAll(candidates);
		}
		//remove from cells at these positions
		candidates.addAll(initial);
		remove(cells);
		//System.out.println(candidates + "\t\t" + initial);
	}
	
	public void remove(int[] cells){
		for(int pos : conflicts) {
			if(cells[pos] != 0) {
				candidates.remove(cells[pos]);
			}
		}
	}
	
	/**
	 * choose the first one left as the solution and remove it
	 * @param data
	 */
	public void solve(int[] data) {
		int peek = tests.pop();
		data[this.loc] = peek;
		show();
	}

	/**
	 * roll back, add the solution back to candidate and erase the solution at data
	 * @param data
	 */
	public void rollback(int[] data){
		data[this.loc] = 0;
		show();
	}

	/**
	 * the candidates' size
	 * @return
	 */
	public int size() {
		return tests.size();
	}

	public void recover() {
		tests.clear();
		for(int x : candidates) {
			tests.push(x);
		}
	}

	private void show() {
		//System.out.println(a);
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("loc:").append(loc).append("r:").append(r).append(" c:").append(c).append(" b:").append(blockLoc);
		buf.append(" / ");
		for(int x : candidates) {
			buf.append(x).append(' ');
		}
		buf.append(" / ");
		for(int x : tests) {
			buf.append(x).append(' ');
		}
		return buf.toString();
	}

}
