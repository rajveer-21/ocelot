#include <SFML/Graphics.hpp>
#include <iostream>
#include <vector>

std::vector button_color_palette{sf::Color::Red, sf::Color::White, sf::Color::Blue};
struct BUTTON
{
    sf::RectangleShape shape;
    sf::Text info;
    BUTTON(sf::Vector2f button_position)
    {
        info.setCharacterSize(50);
        info.setString("CLICK ME FUCKER");
        shape.setSize(sf::Vector2f{100, 100});
        shape.setPosition(button_position);
    }
    void update(sf::Vector2f mousePositionCoords, bool mousePressed)
    {
        if(shape.getGlobalBounds().contains(mousePositionCoords) && mousePressed == true)
        shape.setFillColor(button_color_palette[2]);
        if(shape.getGlobalBounds().contains(mousePositionCoords) && mousePressed == false)
        shape.setFillColor(button_color_palette[0]);
        if(!shape.getGlobalBounds().contains(mousePositionCoords) && mousePressed == false)
        shape.setFillColor(button_color_palette[1]);
    }
};

int main()
{
    sf::RenderWindow window(sf::VideoMode(800, 600), "Ocelot");
    window.setFramerateLimit(60);
    BUTTON button1({10, 450});
    BUTTON button2({210, 450});
    while(window.isOpen())
    {
        bool mousePressed = false;
        sf::Event event;
        while(window.pollEvent(event))
        {
            if(event.type == sf::Event::Closed)
            window.close();
            if(event.type == sf::Event::MouseButtonPressed && event.mouseButton.button == sf::Mouse::Left)
            mousePressed = true;
        }
        sf::Vector2i mousePostionPixels = sf::Mouse::getPosition(window);
        sf::Vector2f mousePostionCoords = window.mapPixelToCoords(mousePostionPixels);
        button1.update(mousePostionCoords, sf::Mouse::isButtonPressed(sf::Mouse::Left));
        if(button1.shape.getGlobalBounds().contains(mousePostionCoords) && sf::Mouse::isButtonPressed(sf::Mouse::Left))
        std::cout << "CLICKED NUMBAH 1" <<std::endl;
        mousePostionPixels = sf::Mouse::getPosition(window);
        mousePostionCoords = window.mapPixelToCoords(mousePostionPixels);
        button2.update(mousePostionCoords, sf::Mouse::isButtonPressed(sf::Mouse::Left));
        if(button2.shape.getGlobalBounds().contains(mousePostionCoords) && sf::Mouse::isButtonPressed(sf::Mouse::Left))
        std::cout << "CLICKED NUMBAH 2" <<std::endl;
        window.clear(sf::Color::Black);
        window.draw(button1.shape);
        window.draw(button2.shape);
        window.display();
    }

}
