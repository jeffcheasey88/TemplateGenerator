package be.jeffcheasey88.template.works;

import dev.peerat.parser.Parser;
import dev.peerat.parser.Token;
import dev.peerat.parser.TokenType;
import dev.peerat.parser.Tokenizer;
import dev.peerat.parser.state.InitialStateTree;
import dev.peerat.parser.state.RedirectStateTree;
import dev.peerat.parser.state.StateTree;

public class ControllerParser extends Parser<Controller>{
	
	public ControllerParser(){
		setTokenizer(new Tokenizer());
		
		InitialStateTree<Controller> loading = new InitialStateTree<>();
		StateTree<Controller> view = new StateTree<>();
		StateTree<Controller> namedView = view.then((validator) -> validator.validate(
				(token) -> token.getType().equals(TokenType.NAME),
				(bag, token) -> {
					Token name = bag.get("file");
					if(name == null) bag.set("file", token);
					else bag.set("file", name.concat(token));
				}));
		namedView.then((validator) -> validator.validate((token) -> token.getValue().equals("."), (bag, token) -> bag.set("file", bag.<Token>get("file").concat(token))))
			.then(namedView);
		namedView.end((controller, bag) -> {
			controller.load(bag.<Token>get("file").getValue(), null);
			return controller;
		});
		namedView.then((validator) -> validator.validate((token) -> token.getValue().equals("-")) && validator.validate((token) -> token.getValue().equals(">")))
			.then((validator) -> validator.validate(
					(token) -> token.getType().equals(TokenType.NAME),
					(bag, token) -> bag.set("name", token)))
			.end((controller, bag) -> {
				controller.load(bag.<Token>get("file").getValue(), bag.<Token>get("name").getValue());
				return controller;
			});
		loading.multiple(view);
		loading.then((validator) -> validator.validate((token) -> token.getValue().equals("}")))
			.end();
		
		InitialStateTree<Controller> checking = new InitialStateTree<>();
		StateTree<Controller> operation = new StateTree<>();
		StateTree<Controller> op = operation.then((validator) -> validator.validate(
				(token) -> token.getType().equals(TokenType.NAME),
				(bag, token) -> bag.set("name", token.getValue())));
		StateTree<Controller> assign = op.then((validator) -> validator.validate(
				(token) -> token.getValue().equals("="),
				(bag, token) -> bag.set("type", "=")))
					.then((validator) -> validator.validate(
							(token) -> token.getType().equals(TokenType.NAME),
							(bag, token) -> bag.set("value", token.getValue())));
		assign.end((controller, bag) -> controller);
		assign.then((validator) -> validator.validate((token) -> token.getValue().equals(".")))
			.then((validator) -> validator.validate(
					(token) -> token.getType().equals(TokenType.NAME),
					(bag, token) -> bag.set("op", token.getValue())))
			.then((validator) -> validator.validate((token) -> token.getValue().equals("(")))
			.then((validator) -> validator.validate((token) -> token.getValue().equals(")")))
			.end((controller, bag) -> controller.addChecker(bag.get("type"), bag.has("negate"), bag.get("name"), bag.get("value"), bag.get("op")));
		
		StateTree<Controller> op_param = op.then((validator) -> validator.validate((token) -> token.getValue().equals("(")));
		StateTree<Controller> bool = op_param.then((validator) -> validator.validate((token) -> token.getType().equals(TokenType.NAME)));
		op_param.then((validator) -> validator.validate((token) -> token.getValue().equals("!")))
			.then(bool);
		bool.then((validator) -> validator.validate((token) -> token.getValue().equals(")")))
			.end((controller, bag) -> controller)
				.then(new RedirectStateTree<>(operation, (a,b) -> System.out.println("TODO")))
				.end();
		
		op.then((validator) -> validator.validate((token) -> token.getType().equals(TokenType.NAME)))
			.end((controller, bag) -> controller);
		checking.multiple(operation);
		checking.then((validator) -> validator.validate((token) -> token.getValue().equals("}")))
		.end();
		
		InitialStateTree<Controller> building = new InitialStateTree<>();
		StateTree<Controller> build = new StateTree<>();
		StateTree<Controller> output = build.then((validator) -> validator.validate(
				(token) -> token.getType().equals(TokenType.NAME),
				(bag, token) -> bag.set("view", token)))
			.then((validator) -> validator.validate((token) -> token.getValue().equals(">")));
		StateTree<Controller> namedBuild = output.then((validator) -> validator.validate(
				(token) -> token.getType().equals(TokenType.NAME),
				(bag, token) -> {
					Token current = bag.get("out");
					if(current == null) bag.set("out", token);
					else bag.set("out", current.concat(token));
				}));
		StateTree<Controller> delimitedBuild = output.then((validator) -> validator.validate(
				(token) -> token.getType().equals(TokenType.DELIMITER),
				(bag, token) -> {
					Token current = bag.get("out");
					if(current == null) bag.set("out", token);
					else bag.set("out", current.concat(token));
				}));
		namedBuild.end((controller, bag) -> {
			controller.build(bag.<Token>get("view").getValue(), bag.<Token>get("out").getValue());
			return controller;
		});
		namedBuild.then(delimitedBuild);
		delimitedBuild.then(namedBuild);
		delimitedBuild.loop();
		building.multiple(build);
		building.then((validator) -> validator.validate((token) -> token.getValue().equals("}")))
		.end();
		
		StateTree<Controller> base = new StateTree<>();
		base.then((validator) -> validator.validate((token) -> token.getValue().equals("{")))
			.then(new RedirectStateTree<>(loading, (a,b) -> {}))
			.then((validator) -> validator.validate((token) -> token.getValue().equals("{")))
			.then(new RedirectStateTree<>(checking, (a,b) -> {}))
			.then((validator) -> validator.validate((token) -> token.getValue().equals("{")))
			.then(new RedirectStateTree<>(building, (a,b) -> {}))
			.end();
		
		setStateTree(base);
	}

}
