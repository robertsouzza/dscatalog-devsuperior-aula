import { render, screen } from "@testing-library/react";
import ButtonIcon from "..";

//Deveria reinderizar um botão com testo informado"
test('ButtonIcon should render button with given text',() => {
    // ARRANGE
       const text = "Fazer login";
    // ACT
       render(
            <ButtonIcon text ={text} />
       );
      //screen.debug();

    // ASSERT
       expect(screen.getByText(text)).toBeInTheDocument();
       expect(screen.getByTestId("arrow")).toBeInTheDocument();
});