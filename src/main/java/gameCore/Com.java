/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gameCore;

/**
 *
 * @author rash4
 */
sealed interface Com permits Command, CommandTree {
    /**for the list display in console.*/
    default boolean hasParam(){return false;}
}