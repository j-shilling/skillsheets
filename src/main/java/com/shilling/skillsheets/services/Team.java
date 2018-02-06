/*
 * Copyright (C) 2018 Jake Shilling
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.shilling.skillsheets.services;

/**
 * A version of {@link Group} which is used for {@link AccountGroup}s of
 * teacher accounts.
 * 
 * @author jake
 * @param <T> Return type used for method chaining.
 */
public interface Team<T extends Team> extends Group<T> {
}
