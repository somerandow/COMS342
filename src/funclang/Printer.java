package funclang;

import java.util.List;

import funclang.AST.Exp;

public class Printer {
	public void print(Value v) {
		if(v.tostring() != "")
			System.out.println(v.tostring());
	}
	public void print(Exception e) {
		System.out.println(e.toString());
	}
	
	public static class Formatter implements AST.Visitor<String> {
		
		public String visit(AST.AddExp e, Env env) {
			String result = "(+ ";
			for(AST.Exp exp : e.all()) 
				result += exp.accept(this, env) + " ";
			return result + ")";
		}
		
		public String visit(AST.UnitExp e, Env env) {
			return "unit";
		}

		public String visit(AST.NumExp e, Env env) {
			return "" + e.v();
		}
		
		public String visit(AST.StrExp e, Env env) {
			return e.v();
		}
		
		public String visit(AST.BoolExp e, Env env) {
			if(e.v()) return "#t";
			return "#f";
		}

		public String visit(AST.DivExp e, Env env) {
			String result = "(/ ";
			for(AST.Exp exp : e.all()) 
				result += exp.accept(this, env) + " ";
			return result + ")";
		}
				
		public String visit(AST.ReadExp e, Env env) {
			return "(read " + e.file().accept(this, env) + ")";
		}

		public String visit(AST.EvalExp e, Env env) {
			return "(eval " + e.code().accept(this, env) + ")";
		}

		public String visit(AST.MultExp e, Env env) {
			String result = "(* ";
			for(AST.Exp exp : e.all()) 
				result += exp.accept(this, env) + " ";
			return result + ")";
		}
		
		public String visit(AST.Program p, Env env) {
			return "" + p.e().accept(this, env);
		}
		
		public String visit(AST.SubExp e, Env env) {
			String result = "(- ";
			for(AST.Exp exp : e.all()) 
				result += exp.accept(this, env) + " ";
			return result + ")";
		}
		
		public String visit(AST.VarExp e, Env env) {
			return "" + e.name();
		}
		
		public String visit(AST.LetExp e, Env env) {
			String result = "(let (";
			List<String> names = e.names();
			List<Exp> value_exps = e.value_exps();
			int num_decls = names.size();
			for (int i = 0; i < num_decls ; i++) {
				result += " (";
				result += names.get(i) + " ";
				result += value_exps.get(i).accept(this, env) + ")";
			}
			result += ") ";
			result += e.body().accept(this, env) + " ";
			return result + ")";
		}
		
		public String visit(AST.DefineDecl d, Env env) {
			String result = "(define ";
			result += d.name() + " ";
			result += d.value_exp().accept(this, env);
			return result + ")";
		}
		
		public String visit(AST.LambdaExp e, Env env) {
			String result = "(lambda ( ";
			if(e.defParam()!=null){
				int i;
				for(i=0; i<e.formals().size()-2; i++){
					result+=e.formals().get(i) + " ";
				}
				result+= "(" + e.formals().get(i+1) +" = " +
						e.defParam().accept(this, env) + ")";
			}
			else {
				for (String formal : e.formals())
					result += formal + " ";
			}
			result += ") ";
			result += e.body().accept(this, env);
			return result + ")";
		}
		
		public String visit(AST.CallExp e, Env env) {
			String result = "(";
			result += e.operator().accept(this, env) + " ";
			for(AST.Exp exp : e.operands())
				result += exp.accept(this, env) + " ";
			return result + ")";
		}
		
		public String visit(AST.IfExp e, Env env) {
			String result = "(if ";
			result += e.conditional().accept(this, env) + " ";
			result += e.then_exp().accept(this, env) + " ";
			result += e.else_exp().accept(this, env);
			return result + ")";
		}
		
		public String visit(AST.LessExp e, Env env) {
			String result = "(< ";
			result += e.first_exp().accept(this, env) + " ";
			result += e.second_exp().accept(this, env);
			return result + ")";
		}

		public String visit(AST.EqualExp e, Env env) {
			String result = "(= ";
			result += e.first_exp().accept(this, env) + " ";
			result += e.second_exp().accept(this, env);
			return result + ")";
		}
		
		public String visit(AST.GreaterExp e, Env env) {
			String result = "(> ";
			result += e.first_exp().accept(this, env) + " ";
			result += e.second_exp().accept(this, env);
			return result + ")";
		}
		
		public String visit(AST.CarExp e, Env env) {
			String result = "(car ";
			result += e.arg().accept(this, env);
			return result + ")";
		}
		
		public String visit(AST.CdrExp e, Env env) {
			String result = "(cdr ";
			result += e.arg().accept(this, env);
			return result + ")";
		}
		
		public String visit(AST.ConsExp e, Env env) {
			String result = "(cons ";
			result += e.fst().accept(this, env) + " ";
			result += e.snd().accept(this, env);
			return result + ")";
		}
		
		public String visit(AST.ListExp e, Env env) {
			String result = "(list ";
			for(AST.Exp exp : e.elems())
				result += exp.accept(this, env) + " ";
			return result + ")";
		}

		public String visit(AST.NullExp e, Env env) {
			String result = "(null? ";
			result += e.arg().accept(this, env);
			return result + ")";
		}

		@Override
		public String visit(AST.NumPredExp e, Env env) {
			String result = "(number? ";
			result += e.exp().accept(this, env);
			return result + ")";
		}

		@Override
		public String visit(AST.BoolPredExp e, Env env) {
			String result = "(boolean? ";
			result += e.exp().accept(this, env);
			return result + ")";
		}

		@Override
		public String visit(AST.StrPredExp e, Env env) {
			String result = "(string? ";
			result += e.exp().accept(this, env);
			return result + ")";
		}

		@Override
		public String visit(AST.ProcPredExp e, Env env) {
			String result = "(procedure? ";
			result += e.exp().accept(this,env);
			return result + ")";
		}

		@Override
		public String visit(AST.PairPredExp e, Env env) {
			String result = "(pair? ";
			result += e.exp().accept(this,env);
			return result + ")";
		}

		@Override
		public String visit(AST.ListPredExp e, Env env) {
			String result = "(list? ";
			result += e.exp().accept(this,env);
			return result + ")";
		}

		@Override
		public String visit(AST.UnitPredExp e, Env env) {
			String result = "(unit? ";
			result += e.exp().accept(this,env);
			return result + ")";
		}

		@Override
		public String visit(AST.RefExp e, Env env) {
			String result = "(ref ";
			result += e.val_exp().accept(this,env);
			return result + ")";
		}

		@Override
		public String visit(AST.DerefExp e, Env env) {
			String result = "(deref ";
			result += e.loc_exp().accept(this,env);
			return result + ")";
		}

		@Override
		public String visit(AST.SetrefExp e, Env env) {
			String result = "(setref ";
			result += e.loc_exp().accept(this,env) + " ";
			result += e.val_exp().accept(this,env);
			return result + ")";
		}

		@Override
		public String visit(AST.FreeExp e, Env env) {
			String result = "(free ";
			result += e.loc_exp().accept(this,env);
			return result + ")";
		}

		@Override
		public String visit(AST.ArrayExp e, Env env) {
			String output = "(array ";
			List<Exp> dim = e.dims();
			for(Exp exp: dim){
				output += exp.accept(this , env) + " ";
			}
			return output + ")";
		}
		@Override
		public String visit(AST.IndexExp e, Env env) {
			String output = "(index ";
			output += e.arr().accept(this , env) + " ";
			List<Exp> ind = e.idxs();
			for(Exp exp: ind){
				output += exp.accept(this , env) + " ";
			}
			return output + ")";
		}
		@Override
		public String visit(AST.ArrAssignExp e, Env env) {
			String output = "(assign ";
			output += e.arr().accept(this , env) + " ";
			List<Exp> idxs = e.idxs();
			for(Exp exp: idxs){
				output += exp.accept(this , env) + " ";
			}
			output += e.val().accept(this , env);
			return output + ")";
		}
	}

}
