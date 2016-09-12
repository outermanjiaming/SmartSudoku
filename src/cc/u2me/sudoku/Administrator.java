package cc.u2me.sudoku;

import java.io.Reader;
import java.util.Stack;

/**
 * Administrator takes charge of the solution procedural
 * 1.choose one of the Cells to be candidate  
 * 2.let it suppose one of its alternative solution and push it into stack
 * 
 * 3.if any one Cell tell Administrator that it has no solution then the last Cell in the stack will be popped out
 * 4.let the popped one suppose another solution, if it has no solution left then just tell administrator
 * 
 * @author lijiaming
 *
 */
public class Administrator {
	//the origin sudoku data
	int[] data = new int[81];
	//to be solved cells
	Cell[] cells = new Cell[81];
	//the operator stack
	Stack<Cell> stack = new Stack<Cell>();
	//time measure
	long start,end;
	
	/**
	 * find the one has least candidates to solve the data and push it into stack 
	 */
	public void elect() {
		Cell candidate = choose();
		if(candidate == null) {
			endup(candidate);
			
			candidate = stack.pop();
			candidate.rollback(data);
			cells[candidate.loc] = candidate;
		}
		
		while(candidate.size() == 0) {
			candidate.recover();
			//next one in the stack
			candidate = stack.pop();
			candidate.rollback(data);
			cells[candidate.loc] = candidate;
			
		}

		//solve it
		candidate.solve(data);
		stack.push(candidate);
		cells[candidate.loc] = null;
	}
	
	
	public Cell choose() {
		Cell candidate = null;
		for(Cell cell : cells) {
			if(cell == null) {
				continue;
			}
			cell.candidate(data);
			cell.recover();
			if(candidate == null) {
				candidate = cell;
				continue;
			}
			if(candidate.size() > cell.size()) {
				candidate = cell;
			}

		}
		return candidate;
	}
	
	/**
	 * initial of origin data and the Cells to be solved
	 * @param rd
	 * @throws Exception
	 */
	public void create(Reader rd) throws Exception {
        for (int loc=0; loc<data.length; ) {
            int ch = rd.read();
            if (ch < 0) {
                return;
            }
            if (ch == '#') {
                while (ch >= 0 && ch != '\n' && ch != '\r') {
                    ch = rd.read();
                }
            } else if (ch >= '1' && ch <= '9') {
            	data[loc]= ch-'0';
                loc++;
            } else if (ch == '.' || ch == '0') {
            	//未赋值的有候选
            	cells[loc] = new Cell(loc,this);
                loc++;
            }
        }
        start = System.currentTimeMillis();
        return;
    }
	
	/**
	 * end up
	 * @param solutions
	 */
	public void endup(Cell now) {
		for(int x : data) {
			if(x == 0) {
				return;
			}
		}
		end = System.currentTimeMillis();
		System.out.println("total time " + (end - start));
		System.out.println(this);
		System.exit(0);
	}
	
	public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int r=0; r<9; r++) {
            if (r%3 == 0) {
                buf.append("-------------------------\n");
            }
            for (int c=0; c<9; c++) {
                if (c%3 == 0) {
                    buf.append("| ");
                }
                int num = data[r*9+c];
                if (num == 0) {
                    buf.append("- ");
                } else {
                    buf.append(num+" ");
                }
            }
            buf.append("|\n");
        }
        buf.append("-------------------------");
        return buf.toString();
    }
}
